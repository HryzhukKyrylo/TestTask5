package com.natife.data.repoImpl

import com.google.gson.Gson
import com.natife.domain.data.model.UdpDto
import com.natife.domain.data.repo.ConnectServerRepository
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import javax.inject.Inject

 class ConnectRepositoryImpl @Inject constructor() : ConnectServerRepository {

    private val destinationIP = "10.0.2.2" //for comp

    //    private val dstIP = "255.255.255.255" // for phone
    private val destinationPort = 8888
    private var requestCycle = true

    override fun requestIp(): String {
        var datagramSocket: DatagramSocket
        val arrayByte = ByteArray(1024)
        val gson = Gson()

        while (requestCycle) {
            try {
                datagramSocket = DatagramSocket().apply {
                    soTimeout = 5000
                }
                val requestPacket = DatagramPacket(arrayByte, arrayByte.size)
                val datagramPacket =
                    DatagramPacket(
                        arrayByte,
                        arrayByte.size,
                        InetAddress.getByName(destinationIP),
                        destinationPort
                    )
                datagramSocket.send(datagramPacket)
                datagramSocket.receive(requestPacket)
                val serverResponse = String(requestPacket.data, 0, requestPacket.length)
                val requestedIp: UdpDto = gson.fromJson(serverResponse, UdpDto::class.java)
                return requestedIp.ip
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return ""
    }
}
