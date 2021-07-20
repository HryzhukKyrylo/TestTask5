package com.natife.testtask5.data.repository

import com.google.gson.Gson
import com.natife.testtask5.data.model.UdpDto
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import javax.inject.Inject

class ConnectRepositoryImpl @Inject constructor() : ConnectServerRepository {

    private val mBuf: ByteArray by lazy { ByteArray(1024) }

//    private val dstIP = "10.0.2.2" //for comp

    private val dstIP = "255.255.255.255" // for phone
    private val dstPort = 8888

    private var ip = ""
    private var rerequest = true

    override fun sendPacket() {
        var mSocket: DatagramSocket
        val sendPacket = DatagramPacket(mBuf, mBuf.size, InetAddress.getByName(dstIP), dstPort)
        val listenPacket = DatagramPacket(mBuf, mBuf.size)
        val gson = Gson()

        while (rerequest) {
            try {
                mSocket = DatagramSocket().apply {
                    soTimeout = 5000
                }
                mSocket.send(sendPacket)
                mSocket.receive(listenPacket)
                val result = String(listenPacket.data, 0, listenPacket.length)
                val res: UdpDto = gson.fromJson(result, UdpDto::class.java)
                ip = res.ip
                return
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun getIp() = ip

    override fun stopSend() {
        rerequest = false
    }
}
