package com.natife.testtask5.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ListRepositoryImpl @Inject constructor(
    private val connectToServer: ConnectServerRepository,
    private val work: WorkServerRepository
) {

    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun connect() : Boolean{
        withContext(Dispatchers.Default) {
            async { connectToServer.sendPacket() }
            val result = async { connectToServer.receivePacket() }
            if (result.await()) {
                connectToServer.stopSend()
                work.connectSocket(connectToServer.getIp())
                return@withContext true

            }else{

            }

        }
        Log.i("TAG", "connect: END")
        return true
    }

    suspend fun fetchUsers() {
        work.fetchUsers()
    }
}
