package com.natife.testtask5.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject


class ListRepository @Inject constructor(
    private val connectToServer: ConnectToServer,
    private val work: WorkWithServer
) {
    private var ip = ""

    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun connect()  {
        withContext(Dispatchers.Default) {
            async { connectToServer.sendPacket() }
            val result = async { connectToServer.receivePacket() }
            if (result.await()) {
                connectToServer.stopSend()
                ip = connectToServer.getIp()
                work.connectSocket(ip)
            }
        }
    }
}
