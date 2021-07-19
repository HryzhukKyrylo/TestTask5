package com.natife.testtask5.data.repository

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.natife.testtask5.data.model.MessageDto
import com.natife.testtask5.util.CustomScope
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import model.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.InetAddress
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.*

class WorkServerRepository {

    private val scope = CustomScope()
    private lateinit var mSocket: Socket
    private val writer: PrintWriter by lazy {
        PrintWriter(
            OutputStreamWriter(mSocket.getOutputStream()),
            true
        )
    }
    private val reader: BufferedReader by lazy { BufferedReader(InputStreamReader(mSocket.getInputStream())) }

    private val port = 6666
    private var id = ""
    private var nameDto = ""
    private val gson = Gson()

    private var listenUser = true


    private val _users = MutableSharedFlow<List<User>>(
        replay = 1,
        extraBufferCapacity = 10,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val users: SharedFlow<List<User>> = _users

    private val _messages = MutableSharedFlow<Payload>(
        replay = 1,
        extraBufferCapacity = 10,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val messages: SharedFlow<Payload> = _messages

    @SuppressLint("SimpleDateFormat")
    suspend fun sendMyMessage(idUser: String, message: String) {
        val sdf = SimpleDateFormat("hh:mm:ss")
        val currentDate = sdf.format(Date())
        val messageDTO: String =
            gson.toJson(
                SendMessageDto(
                    id = id,
                    receiver = idUser,
                    message = message,
                    time = currentDate
                )
            )
        val actionMessage: String = gson.toJson(BaseDto(BaseDto.Action.SEND_MESSAGE, messageDTO))
        sendMessageToServer(actionMessage)
        _messages.emit(
            SendMessageDto(
                id = id,
                receiver = idUser,
                message = message,
                time = currentDate
            )
        )
    }

    fun connectSocket(ip: String, nickname: String) {
        mSocket = Socket(InetAddress.getByName(ip), port)
        nameDto = nickname
        startListen()
    }

    private fun sendConnect() {
        val connect: String = gson.toJson(ConnectDto(id = id, name = nameDto))
        val messagePing: String = gson.toJson(BaseDto(BaseDto.Action.CONNECT, connect))
        sendMessageToServer(messagePing)
    }

    private suspend fun startPing() {
        scope.launch {
            val ping: String = gson.toJson(PingDto(id = id))
            val messagePing: String = gson.toJson(BaseDto(BaseDto.Action.PING, ping))
            while (true) {
                sendMessageToServer(messagePing)
                delay(10000L)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun startListen() {
        scope.launch(Dispatchers.IO) {
            while (true) {
                val line = reader.readLine()
                val res: BaseDto = gson.fromJson(line, BaseDto::class.java)

                when (res.action) {
                    BaseDto.Action.CONNECTED -> {
                        val c: ConnectedDto = gson.fromJson(res.payload, ConnectedDto::class.java)
                        id = c.id
                        sendConnect()
                        startPing()
                    }
                    BaseDto.Action.NEW_MESSAGE -> {
                        val sdf = SimpleDateFormat("hh:mm:ss")
                        val currentDate = sdf.format(Date())
                        val newMessage: MessageDto =
                            gson.fromJson(res.payload, MessageDto::class.java)
                                .apply { time = currentDate }
                        _messages.emit(newMessage)
                    }
                    BaseDto.Action.USERS_RECEIVED -> {
                        val receivedUsers: UsersReceivedDto =
                            gson.fromJson(res.payload, UsersReceivedDto::class.java)
                        _users.emit(receivedUsers.users)
                    }
                    else -> {
                    }
                }
            }
        }
    }

    private fun sendMessageToServer(message: String) {
        writer.println(message)
    }

    suspend fun fetchUsers() {
        val getUsers: String = gson.toJson(GetUsersDto(id = id))
        val message: String = gson.toJson(BaseDto(BaseDto.Action.GET_USERS, getUsers))
        startGetUsers(message)
    }

    private suspend fun startGetUsers(message: String) {
        scope.launch {
            while (listenUser) {
                sendMessageToServer(message)
                delay(10000L)
            }
        }
    }

//    fun disconnect() {
//        reader.close()
//        writer.close()
//        mSocket.close()
//        mSocket = null
//        scope.stop()
//    }
}
