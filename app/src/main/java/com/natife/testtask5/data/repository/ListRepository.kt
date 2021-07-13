package com.natife.testtask5.data.repository

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.*
import model.UdpDto
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import javax.inject.Inject


class ListRepository @Inject constructor(
    private val connectToServer: ConnectToServer
) {
    suspend fun connect() {
        withContext(Dispatchers.Default) {
            async { connectToServer.sendPacket() }
            val result = async { connectToServer.receivePacket() }
            if (result.await()) {
                connectToServer.stopSend()
            }

            Log.i("TAGTAGTAG", "sendViewModel: ${result.await()}")
        }
    }
}
