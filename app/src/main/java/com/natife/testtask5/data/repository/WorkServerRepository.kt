package com.natife.testtask5.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.natife.testtask5.util.CustomScope
import kotlinx.coroutines.*
import model.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.InetAddress
import java.net.Socket

class WorkServerRepository {
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
    val gson = Gson()

    val users = MutableLiveData<List<User>>()


    suspend fun connectSocket(ip: String, nickname: String): String {
        mSocket = Socket(InetAddress.getByName(ip), port)
        val line = reader.readLine()
        val res: BaseDto = gson.fromJson(line, BaseDto::class.java)
        val c: ConnectedDto = gson.fromJson(res.payload, ConnectedDto::class.java)
        id = c.id
        nameDto = nickname

        sendConnect()
        startPing()
        startListen()
        return id
    }


    private fun sendConnect() {
        val connect: String = gson.toJson(ConnectDto(id = id, name = nameDto))
        val messagePing: String = gson.toJson(BaseDto(BaseDto.Action.CONNECT, connect))
        sendMessage(messagePing)

    }

    private suspend fun startPing() {
        CustomScope().launch {
            val ping: String = gson.toJson(PingDto(id = id))
            val messagePing: String = gson.toJson(BaseDto(BaseDto.Action.PING, ping))
            while (true) {
                sendMessage(messagePing)
                delay(10000L)
            }
        }
    }

    private fun startListen() {
        CustomScope().launch {
            while (true) {
                val line = reader.readLine()
                val res: BaseDto = gson.fromJson(line, BaseDto::class.java)

                when(res.action){
                    BaseDto.Action.NEW_MESSAGE -> {
                        Log.i("TAG", "WorkServerRepository/startListen:  NEW_MESSAGE")
                    }
                    BaseDto.Action.USERS_RECEIVED -> {
                        Log.i("TAG", "WorkServerRepository/startListen:  USERS_RECEIVED")
                        val receivedUsers: UsersReceivedDto = gson.fromJson(res.payload, UsersReceivedDto::class.java)
                        Log.i("TAG", "WorkServerRepository/startListen:  USERS_RECEIVED = $receivedUsers")
                        users.postValue(receivedUsers.users)
                    }
                    else -> {
                        Log.i("TAG", "WorkServerRepository/startListen:  ${res.action}")
                    }
                }
                delay(10000L)
            }
        }
    }

    private fun sendMessage(message: String) {
        writer.println(message)
//        writer.flush()
    }

    suspend fun fetchUsers() {
        Log.i("TAG", "WorkServerRepository/fetchUsers: -----fetchUsers-----")

        val getUsers: String = gson.toJson(GetUsersDto(id = id))
        val message: String = gson.toJson(BaseDto(BaseDto.Action.GET_USERS, getUsers))
        Log.i("TAG", "WorkServerRepository/fetchUsers: GET_USERS - $message")

        sendMessage(message)
//        val line = reader.readLine()
//        Log.i("TAG", "WorkServerRepository/fetchUsers: result - $line")
//         val res: BaseDto = gson.fromJson(line, BaseDto::class.java)
//         val c: UsersReceivedDto = gson.fromJson(res.payload, UsersReceivedDto::class.java)
//         Log.i("TAG", "WorkServerRepository/fetchUsers: UsersReceivedDto - $c")

//        val res: BaseDto = gson.fromJson(line, BaseDto::class.java)
//        val c: ConnectedDto = gson.fromJson(res.payload, ConnectedDto::class.java)
    }

    fun getUsers() :LiveData<List<User>>{
        return users
    }


}
