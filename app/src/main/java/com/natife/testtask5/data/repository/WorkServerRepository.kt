package com.natife.testtask5.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import model.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.InetAddress
import java.net.Socket

class WorkServerRepository {
    private lateinit var mSocket: Socket
    private val writer: PrintWriter by lazy {  PrintWriter(OutputStreamWriter(mSocket.getOutputStream())) }
    private val reader : BufferedReader by lazy {  BufferedReader(InputStreamReader(mSocket.getInputStream())) }

    private val port = 6666
    private var id = ""
    private var nameDto = "Kiri"
    val gson = Gson()

    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun connectSocket(ip: String) {
        mSocket = Socket(InetAddress.getByName(ip), port)
        val line = reader.readLine()
        val res: BaseDto = gson.fromJson(line, BaseDto::class.java)
        val c: ConnectedDto = gson.fromJson(res.payload, ConnectedDto::class.java)
        id = c.id
        sendConnect()
        startPing()
    }

    private fun sendConnect() {
        val connect: String = gson.toJson(ConnectDto(id = id, name = nameDto))
        val messagePing: String = gson.toJson(BaseDto(BaseDto.Action.CONNECT, connect))
        sendMessage(messagePing)

    }

    private suspend fun startPing() {
        val ping: String = gson.toJson(PingDto(id = id))
        val messagePing: String = gson.toJson(BaseDto(BaseDto.Action.PING, ping))
        while (true) {
            sendMessage(messagePing)
            delay(10000L)
        }
    }

    private fun sendMessage(message: String) {

        writer.println(message)
        writer.flush()
    }

    suspend fun fetchUsers() {
        Log.i("TAG", "fetchUsers: ")

        val getUsers: String = gson.toJson(GetUsersDto(id = id))
        val message: String = gson.toJson(BaseDto(BaseDto.Action.CONNECT, getUsers))
        sendMessage(message)
        val line = reader.readLine()
        Log.i("TAG", "fetchUsers: $line")
//        val res: BaseDto = gson.fromJson(line, BaseDto::class.java)
//        val c: ConnectedDto = gson.fromJson(res.payload, ConnectedDto::class.java)
    }


}
