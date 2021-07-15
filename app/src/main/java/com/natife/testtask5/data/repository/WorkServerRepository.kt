package com.natife.testtask5.data.repository

import android.util.Log
import com.google.gson.Gson
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
//    private var id = ""
    private var id = "192.168.1.103"
//    private var nameDto = ""
    private var nameDto = "NickName"
    private val gson = Gson()

    private val _users = MutableSharedFlow<List<User>>(replay = 1, extraBufferCapacity = 10, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val users : SharedFlow<List<User>> = _users

    fun connectSocket(ip: String, nickname: String) {
        mSocket = Socket(InetAddress.getByName(ip), port)
        nameDto = nickname
        startListen()
    }

    private fun sendConnect() {
        val connect: String = gson.toJson(ConnectDto(id = id, name = nameDto))
        val messagePing: String = gson.toJson(BaseDto(BaseDto.Action.CONNECT, connect))
        sendMessage(messagePing)
        Log.i("TAG", "WorkServerRepository/sendConnect:  sendMessage - CONNECT")
    }

    private suspend fun startPing() {
        scope.launch {
            val ping: String = gson.toJson(PingDto(id = id))
            val messagePing: String = gson.toJson(BaseDto(BaseDto.Action.PING, ping))
            while (true) {
                sendMessage(messagePing)
                Log.i("TAG", "WorkServerRepository/startPing:  sendMessage - PING")
                delay(10000L)
            }
        }
    }

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
                        Log.i("TAG", "WorkServerRepository/startListen:  NEW_MESSAGE")
                    }
                    BaseDto.Action.USERS_RECEIVED -> {
                        Log.i(
                            "TAG", "WorkServerRepository/startListen: USERS_RECEIVED -> $res "
                        )

                        val receivedUsers: UsersReceivedDto =
                            gson.fromJson(res.payload, UsersReceivedDto::class.java)
                        _users.emit(receivedUsers.users)

                        Log.i(
                            "TAG",
                            "WorkServerRepository/startListen:  USERS_RECEIVED = $receivedUsers"
                        )
                    }
                    else -> {
                        Log.i("TAG", "WorkServerRepository/startListen:  ${res.action}")
                    }
                }
            }
        }
    }

    private fun sendMessage(message: String) {
        writer.println(message)
//        writer.flush()
    }

    fun fetchUsers() {
        val getUsers: String = gson.toJson(GetUsersDto(id = id))
        val message: String = gson.toJson(BaseDto(BaseDto.Action.GET_USERS, getUsers))
        sendMessage(message)
        Log.i("TAG", "WorkServerRepository/fetchUsers: sendMessage - GET_USERS")
    }

//    fun disconnect() {
//        reader.close()
//        writer.close()
//        mSocket.close()
//        mSocket = null
//        scope.stop()
//    }
}
