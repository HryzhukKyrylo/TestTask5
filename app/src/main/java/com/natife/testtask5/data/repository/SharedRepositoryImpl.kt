package com.natife.testtask5.data.repository

import android.util.Log
import javax.inject.Inject


class SharedRepositoryImpl @Inject constructor(
    private val connectToServer: ConnectServerRepository,
    private val workWithServer: WorkServerRepository
) {

    suspend fun connect(nickname: String) : String{
        connectToServer.sendPacket()
        connectToServer.stopSend()
        val id =  workWithServer.connectSocket(connectToServer.getIp(), nickname)
        return id
    }

    suspend fun fetchUsers(){
        workWithServer.fetchUsers()
    }

    suspend fun getUsers() = workWithServer.getUsers()
}
