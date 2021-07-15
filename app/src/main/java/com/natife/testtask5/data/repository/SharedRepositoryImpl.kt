package com.natife.testtask5.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import model.User
import model.UsersReceivedDto
import javax.inject.Inject


class SharedRepositoryImpl @Inject constructor(
    private val connectToServer: ConnectServerRepository,
    private val workWithServer: WorkServerRepository
) {

    val users = workWithServer.users

    suspend fun connect(nickname: String): String {
        connectToServer.sendPacket()
        connectToServer.stopSend()
        val id = workWithServer.connectSocket(connectToServer.getIp(), nickname)
        return id
    }

    fun fetchUsers() {
        workWithServer.fetchUsers()
    }
}

