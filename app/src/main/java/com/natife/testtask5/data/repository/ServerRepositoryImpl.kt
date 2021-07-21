package com.natife.testtask5.data.repository

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.natife.testtask5.data.model.*
import com.natife.testtask5.util.CustomScope
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.InetAddress
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ServerRepositoryImpl @Inject constructor() : ServerRepository {

    private val scope = CustomScope()
    private lateinit var socket: Socket
    private val writer: PrintWriter by lazy {
        PrintWriter(
            OutputStreamWriter(socket.getOutputStream()),
            true
        )
    }
    private val reader: BufferedReader by lazy { BufferedReader(InputStreamReader(socket.getInputStream())) }

    private val port = 6666
    private var myId = ""
    private var myName = ""
    private val gson = Gson()

    private var cyclePing = true
    private var cycleListen = true

    private val listenToUsers = MutableSharedFlow<List<User>>(
        replay = 1,
        extraBufferCapacity = 10,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private val users: SharedFlow<List<User>> = listenToUsers

    private val listenToMessages = MutableSharedFlow<MessageDto>(
        replay = 1,
        extraBufferCapacity = 10,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private val messages: SharedFlow<MessageDto> = listenToMessages


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
        listenToMessages.emit(objectMessageDto)
        sendMessageToServer(actionMessage)
    }

    override fun connectSocket(ip: String, nickname: String) {
        socket = Socket(InetAddress.getByName(ip), port)
        myName = nickname
        startListenServer()
    }

    override fun getUsers(): SharedFlow<List<User>> = users

    override fun getMessages(): SharedFlow<MessageDto> = messages

    override fun getId(): String = myId

    override suspend fun fetchUsers() {
        val getUsers: String = gson.toJson(GetUsersDto(id = myId))
        val message: String = gson.toJson(BaseDto(BaseDto.Action.GET_USERS, getUsers))
        sendMessageToServer(message)
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
            while (cycleListen) {
                val line = reader.readLine()
                val res: BaseDto = gson.fromJson(line, BaseDto::class.java)

                when (res.action) {
                    BaseDto.Action.CONNECTED -> {
                        val c: ConnectedDto = gson.fromJson(res.payload, ConnectedDto::class.java)
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
                        listenToMessages.emit(newMessage)
                    }
                    BaseDto.Action.USERS_RECEIVED -> {
                        val receivedUsers: UsersReceivedDto =
                            gson.fromJson(res.payload, UsersReceivedDto::class.java)
                        listenToUsers.emit(receivedUsers.users)
                    }
                    else -> {
                    }
                }
            }
        }
    }

    override suspend fun disconnect() {
        val disconnectDto: String = gson.toJson(DisconnectDto(id = myId, code = 1))
        val messagePing: String = gson.toJson(BaseDto(BaseDto.Action.CONNECT, disconnectDto))
        sendMessageToServer(messagePing)
    }

    private fun sendMessageToServer(message: String) {
        writer.println(message)
    }

//    fun disconnect() {
//        reader.close()
//        writer.close()
//        mSocket.close()
//        mSocket = null
//        scope.stop()
//    }
}
