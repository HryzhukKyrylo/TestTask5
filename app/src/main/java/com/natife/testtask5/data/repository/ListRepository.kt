package com.natife.testtask5.data.repository

import android.util.Log
import kotlinx.coroutines.delay
import model.UdpDto
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress


class ListRepository {
    private val socket = DatagramSocket()

    val sendData = "request".toByteArray()
    val packet =
        DatagramPacket(sendData, sendData.size, InetAddress.getByName("255.255.255.255"), 8888)

    suspend fun sendPacket(): String {
        var ip = ""
        try {
            socket.send(packet)

            Log.i("TAGTAGTAG", "sendPacket: ---------$ip")

            socket.receive(packet)
//            val received = String(packet.getData(), 0, 10)
            val received = String(packet.getData(), 0, packet.getLength())
//            val received = packet.socketAddress.toString()

            ip = received
            Log.i("TAGTAGTAG", "sendPacket: $received")
        } catch (e: IOException) {
            Log.e("ListRepositoryTAG", "sendPacket: ${e.printStackTrace()}")
        }
        return ip
    }

}
