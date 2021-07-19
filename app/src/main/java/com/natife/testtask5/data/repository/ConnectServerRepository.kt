package com.natife.testtask5.data.repository

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.delay
import model.UdpDto
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class ConnectServerRepository {
    private lateinit var mSocket: DatagramSocket

    lateinit var listenPacket: DatagramPacket
    private lateinit var sendPacket: DatagramPacket

    private val dstIP = "10.0.2.2" //for comp
//    private val dstIP = "255.255.255.255" // for phone
    private val dstPort = 8888
    private val mBuf = ByteArray(1024)

    private var ip = ""


    private var rerequest = true

     fun sendPacket() {
        sendPacket = DatagramPacket(mBuf, mBuf.size, InetAddress.getByName(dstIP), dstPort)
        listenPacket = DatagramPacket(mBuf, mBuf.size)
        var result = ""
        val gson = Gson()

        while (rerequest) {
            try {
                mSocket = DatagramSocket().apply {
                    soTimeout = 5000
                }
                mSocket.send(sendPacket)
                mSocket.receive(listenPacket)
                result = String(listenPacket.data, 0, listenPacket.length)
                val res: UdpDto = gson.fromJson(result, UdpDto::class.java)
                ip = res.ip
                Log.i("TAGTAGTAG", "ConnectServerRepository/sendPacket: ip = $ip")
                return
            } catch (e: IOException) {
                Log.i("TAGTAGTAG", "ConnectServerRepository/sendPacket: exception - ${e.message}")
            }
        }
    }

    fun getIp() = ip

    fun stopSend() {
        rerequest = false
    }
}
