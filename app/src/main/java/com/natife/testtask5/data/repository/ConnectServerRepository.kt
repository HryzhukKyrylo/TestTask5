package com.natife.testtask5.data.repository


interface ConnectServerRepository {
    fun sendPacket()
    fun getIp():String
    fun stopSend()
}
