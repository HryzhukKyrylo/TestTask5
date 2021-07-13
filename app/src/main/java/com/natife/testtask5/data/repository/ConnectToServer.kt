package com.natife.testtask5.data.repository

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.delay
import model.UdpDto
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

/**
 *@author admin
 */class ConnectToServer {
    private lateinit var mSocket: DatagramSocket

    lateinit var listenPacket: DatagramPacket
    private lateinit var sendPacket: DatagramPacket

    private val dstIP = "255.255.255.255"
    private val dstPort = 8888
    private val mBuf = ByteArray(1024)

    private var ip = ""


    private var rerequest = true

    suspend fun sendPacket() {
        sendPacket = DatagramPacket(mBuf, mBuf.size, InetAddress.getByName(dstIP), dstPort)
        mSocket = DatagramSocket()

        while (rerequest) {
            try {
                mSocket.send(sendPacket)
                Log.i("TAGTAGTAG", "sendPacket: ")
                delay(1000L)
            } catch (e: IOException) {
                Log.i("TAGTAGTAG", "sendPacket: ${e.message}")
            }
        }
    }

    suspend fun receivePacket(): Boolean {
        Log.i("TAGTAGTAG", "receivePacket")

        listenPacket = DatagramPacket(mBuf, mBuf.size)

        mSocket = DatagramSocket()
        var result = ""
        var gson = Gson()
        try {
            mSocket.receive(listenPacket)
            result = listenPacket.data.toString()
            result = String(listenPacket.data,  0, listenPacket.length)

            val res: UdpDto = gson.fromJson(result, UdpDto::class.java)
            ip = res.ip

            Log.i("TAGTAGTAG", "receivePacket: $res")

        } catch (e: IOException) {
            Log.i("TAGTAGTAG", "receivePacket: ${e.message}")
            return false
        }

        return true
    }

    fun getIp() = ip

    fun stopSend() {
        rerequest = false
    }
}
