package com.natife.testtask5.data.repository

import android.annotation.SuppressLint
import android.util.Log
import com.google.gson.Gson
import com.natife.testtask5.data.model.*
import com.natife.testtask5.util.CustomScope
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.lang.Exception
import java.net.Socket
import java.net.SocketException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ServerRepositoryImpl @Inject constructor() : ServerRepository {

    private val scope = CustomScope()
    private var pongJob: Job? = null

    private lateinit var socket: Socket
    private lateinit var writer: PrintWriter

    //    private val reader: BufferedReader by lazy { BufferedReader(InputStreamReader(socket.getInputStream())) }
    private lateinit var reader: BufferedReader

    private val port = 6666
    private var serverIP = ""
    private var myId = ""
    private var myName = ""
    private val gson = Gson()

    private var cyclePing = true
    private var cycleListen = true
    private var cycleUsers = true


    private val mutableUsers = MutableSharedFlow<List<User>>(
        replay = 1,
        extraBufferCapacity = 10,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private val users: SharedFlow<List<User>> = mutableUsers

    private val mutableMessages = MutableSharedFlow<MessageDto>(
        replay = 1,
        extraBufferCapacity = 10,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private val messages: SharedFlow<MessageDto> = mutableMessages

    private val mutableConnection = MutableSharedFlow<Boolean>(
        replay = 1,
        extraBufferCapacity = 10,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private val connection: SharedFlow<Boolean> = mutableConnection


    @SuppressLint("SimpleDateFormat")
    override suspend fun sendMyMessage(idUser: String, message: String) {
        val sdf = SimpleDateFormat("hh:mm:ss")
        val currentDate = sdf.format(Date())
        val objectSendMessageDto =
            SendMessageDto(id = myId, receiver = idUser, message = message, time = currentDate)
        val objectMessageDto =
            MessageDto(from = User(id = myId, name = myName), message = message, time = currentDate)
        val jsonSenMessageDto: String = gson.toJson(objectSendMessageDto)
        val actionMessage: String =
            gson.toJson(BaseDto(BaseDto.Action.SEND_MESSAGE, jsonSenMessageDto))
        sendMessageToServer(actionMessage)
        mutableMessages.emit(objectMessageDto)
    }

    override fun connectSocket(ip: String, nickname: String) {
        try {
            serverIP = ip
            socket = Socket(serverIP, port)
            myName = nickname
            startListenServer()

        } catch (socket: SocketException) {
            socket.printStackTrace()
        }
    }

    override fun getUsers(): SharedFlow<List<User>> = users

    override fun getMessages(): SharedFlow<MessageDto> = messages

    override fun getConnection(): SharedFlow<Boolean> = connection

    override fun getId(): String = myId

    override suspend fun fetchUsers() {
        val getUsers: String = gson.toJson(GetUsersDto(id = myId))
        val message: String = gson.toJson(BaseDto(BaseDto.Action.GET_USERS, getUsers))
        cycleUsers = true
        scope.launch {
            while (cycleUsers) {
                sendMessageToServer(message)
                delay(10000L)
            }
        }
    }

    override fun stopFetchUsers() {
        cycleUsers = false
    }

    private fun sendConnect() {
        val connect: String = gson.toJson(ConnectDto(id = myId, name = myName))
        val messagePing: String = gson.toJson(BaseDto(BaseDto.Action.CONNECT, connect))
        sendMessageToServer(messagePing)
    }

    private suspend fun startPing() {
        scope.launch {
            val ping: String = gson.toJson(PingDto(id = myId))
            val messagePing: String = gson.toJson(BaseDto(BaseDto.Action.PING, ping))
            while (cyclePing) {
                sendMessageToServer(messagePing)
                delay(10000L)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun startListenServer() {
        scope.launch(Dispatchers.IO) {
            try {
                reader = BufferedReader(InputStreamReader(socket.getInputStream()))
                while (cycleListen) {
//                    if (reader.ready()) {

                    val line = reader.readLine()

                    val res: BaseDto = gson.fromJson(line, BaseDto::class.java)

                    when (res.action) {
                        BaseDto.Action.CONNECTED -> {
                            val c: ConnectedDto =
                                gson.fromJson(res.payload, ConnectedDto::class.java)
                            myId = c.id
                            sendConnect()
                            startPing()
                        }
                        BaseDto.Action.NEW_MESSAGE -> {
                            val sdf = SimpleDateFormat("hh:mm:ss")
                            val currentDate = sdf.format(Date())
                            val newMessage: MessageDto =
                                gson.fromJson(res.payload, MessageDto::class.java)
                                    .apply { time = currentDate }
                            mutableMessages.emit(newMessage)
                        }
                        BaseDto.Action.USERS_RECEIVED -> {
                            val receivedUsers: UsersReceivedDto =
                                gson.fromJson(res.payload, UsersReceivedDto::class.java)
                            mutableUsers.emit(receivedUsers.users)
                        }
                        BaseDto.Action.PONG -> {
                            handlerPong()
                        }
                        else -> {
                        }
                    }
//                    } else {
//                        continue
//                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun disconnect() {
        val disconnectDto: String = gson.toJson(DisconnectDto(id = myId, code = 1))
        val messagePing: String = gson.toJson(BaseDto(BaseDto.Action.DISCONNECT, disconnectDto))
        sendMessageToServer(messagePing)
        close()
    }

    private fun sendMessageToServer(message: String) {
        try {
            writer = PrintWriter(OutputStreamWriter(socket.getOutputStream()), true)
            writer.println(message)

            Log.i("TAG", "ServerRepositoryImpl/sendMessageToServer: $message")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private suspend fun handlerPong() {
        pongJob?.cancel()
        pongJob = scope.launch {
            delay(15000L)
            withContext(Dispatchers.Main) {
                mutableConnection.emit(false)
            }
            disconnect()
        }
    }

    private fun close() {
        reader.close()
        writer.close()
        socket.close()
        scope.stop()
    }
}
