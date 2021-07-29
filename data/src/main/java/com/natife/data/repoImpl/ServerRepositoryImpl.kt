package com.natife.data.repoImpl

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.natife.domain1.data.model.*
import com.natife.domain1.data.repo.ServerRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
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
import kotlin.coroutines.CoroutineContext

class ServerRepositoryImpl @Inject constructor() : ServerRepository {

    private val customScope = CustomScope()
    private var pongJob: Job? = null

    private var socket: Socket? = null
    private var printWriter: PrintWriter? = null
    private var bufferedReader: BufferedReader? = null

    private val serverPort = 6666
    private var serverIP = ""
    private var myId = ""
    private var myName = ""
    private val gson = Gson()

    private var cyclePing = true
    private var cycleListen = true
    private var cycleUsers = true


    private val mutableUsers = MutableSharedFlow<List<User>>(
        replay = 1, extraBufferCapacity = 10, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private val observeUsers: SharedFlow<List<User>> = mutableUsers

    private val mutableMessages = MutableSharedFlow<MessageDto>(
        replay = 1, extraBufferCapacity = 10, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private val observeMessages: SharedFlow<MessageDto> = mutableMessages

    private val mutableConnection = MutableSharedFlow<Boolean>(
        replay = 1, extraBufferCapacity = 10, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private val observeConnection: SharedFlow<Boolean> = mutableConnection


    @SuppressLint("SimpleDateFormat")
    override suspend fun sendMyMessage(idUser: String, message: String) {
        val dateFormat = SimpleDateFormat("hh:mm:ss")
        val currentDate = dateFormat.format(Date())
        val objectSendMessageDto =
            SendMessageDto(id = myId, receiver = idUser, message = message, time = currentDate)
        val objectMessageDto =
            MessageDto(from = User(id = myId, name = myName), message = message, time = currentDate)
        val jsonSendMessageDto: String = gson.toJson(objectSendMessageDto)
        val actionMessage: String =
            gson.toJson(BaseDto(BaseDto.Action.SEND_MESSAGE, jsonSendMessageDto))
        sendMessageToServer(actionMessage)
        mutableMessages.emit(objectMessageDto)
    }

    override suspend fun connectSocket(ip: String, nickname: String) {
        try {
            serverIP = ip
            socket = Socket(serverIP, serverPort)
            myName = nickname
            mutableConnection.emit(true)
            startListenToServer()

        } catch (socketException: SocketException) {
            socketException.printStackTrace()
        }
    }

    override fun getUsers(): Flow<List<User>> = observeUsers

    override fun getMessages(): Flow<MessageDto> = observeMessages

    override fun getConnection(): Flow<Boolean> = observeConnection

    override fun getId(): String = myId

    override suspend fun startRequestingUsers() {
        val objectGetUsersDto: String = gson.toJson(GetUsersDto(id = myId))
        val actionMessage: String =
            gson.toJson(BaseDto(BaseDto.Action.GET_USERS, objectGetUsersDto))
        cycleUsers = true
        customScope.launch {
            while (cycleUsers) {
                sendMessageToServer(actionMessage)
                delay(15000L)
            }
        }
    }

    override fun stopRequestingUsers() {
        cycleUsers = false
    }

    private fun sendConnectToServer() {
        val objectConnectDto: String = gson.toJson(ConnectDto(id = myId, name = myName))
        val actionMessage: String = gson.toJson(BaseDto(BaseDto.Action.CONNECT, objectConnectDto))
        sendMessageToServer(actionMessage)
    }

    private suspend fun startPing() {
        customScope.launch {
            val objectPingDto: String = gson.toJson(PingDto(id = myId))
            val actionMessage: String = gson.toJson(BaseDto(BaseDto.Action.PING, objectPingDto))
            while (cyclePing) {
                sendMessageToServer(actionMessage)
                delay(10000L)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun startListenToServer() {
        customScope.launch(Dispatchers.IO) {
            try {
                if (bufferedReader == null) {
                    bufferedReader = BufferedReader(InputStreamReader(socket?.getInputStream()))
                }
                while (cycleListen) {
                    val readLine = bufferedReader?.readLine()

                    val responseFromServer: BaseDto? = gson.fromJson(readLine, BaseDto::class.java)

                    when (responseFromServer?.action) {
                        BaseDto.Action.CONNECTED -> {
                            val objectConnectedDto: ConnectedDto =
                                gson.fromJson(responseFromServer.payload, ConnectedDto::class.java)
                            myId = objectConnectedDto.id
                            sendConnectToServer()
                            startPing()
                        }
                        BaseDto.Action.NEW_MESSAGE -> {
                            val dateFormat = SimpleDateFormat("hh:mm:ss")
                            val currentDate = dateFormat.format(Date())
                            val objectMessageDto: MessageDto =
                                gson.fromJson(responseFromServer.payload, MessageDto::class.java)
                                    .apply { time = currentDate }
                            mutableMessages.emit(objectMessageDto)
                        }
                        BaseDto.Action.USERS_RECEIVED -> {
                            val objectUsersReceivedDto: UsersReceivedDto =
                                gson.fromJson(
                                    responseFromServer.payload,
                                    UsersReceivedDto::class.java
                                )
                            mutableUsers.emit(objectUsersReceivedDto.users)
                        }
                        BaseDto.Action.PONG -> {
                            handlerPong()
                        }
                        else -> {
                            //nothing)
                        }
                    }
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
    }

    override suspend fun disconnectFromServer() {
        val objectDisconnectDto: String = gson.toJson(DisconnectDto(id = myId, code = 1))
        val actionMessage: String =
            gson.toJson(BaseDto(BaseDto.Action.DISCONNECT, objectDisconnectDto))
        sendMessageToServer(actionMessage)
        close()
    }

    private fun sendMessageToServer(message: String) {
        try {
            if (printWriter == null)
                printWriter = PrintWriter(OutputStreamWriter(socket?.getOutputStream()), true)

            printWriter?.println(message)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun handlerPong() {
        pongJob?.cancel()
        pongJob = customScope.launch {
            delay(15000L)
            withContext(Dispatchers.Main) {
                mutableConnection.emit(false)
            }
            disconnectFromServer()
        }
    }

    private fun close() {
        printWriter?.close()
        printWriter = null

        bufferedReader?.close()
        bufferedReader = null

        socket?.close()
        socket = null

        customScope.cancelChildren()
    }
}

class CustomScope : CoroutineScope {

    private var parentJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + parentJob

    fun cancelChildren() {
        parentJob.cancelChildren()
    }
}
