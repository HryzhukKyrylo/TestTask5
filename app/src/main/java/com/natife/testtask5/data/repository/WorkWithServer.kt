package com.natife.testtask5.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import model.BaseDto
import model.ConnectedDto
import model.PingDto
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.InetAddress
import java.net.Socket

class WorkWithServer {
    private lateinit var mSocket: Socket
    private val port = 6666
    private var id = ""
    val gson = Gson()

    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun connectSocket(ip: String) {
        mSocket = Socket(InetAddress.getByName(ip), port)
        val reader = BufferedReader(InputStreamReader(mSocket.getInputStream()))
        val line = reader.readLine()
        val res: BaseDto = gson.fromJson(line, BaseDto::class.java)
        val c: ConnectedDto = gson.fromJson(res.payload, ConnectedDto::class.java)
        id = c.id
        startPing()
    }

    private suspend fun startPing() {
        val ping: String = gson.toJson(PingDto(id = id))
        val messagePing: String = gson.toJson(BaseDto(BaseDto.Action.PING,ping))
        val writer = PrintWriter(OutputStreamWriter(mSocket.getOutputStream()))
        while (true) {
                writer.println(messagePing)
            writer.flush()
                delay(5000L)
                Log.i("TAG", "startPing: ")

        }
    }


}
