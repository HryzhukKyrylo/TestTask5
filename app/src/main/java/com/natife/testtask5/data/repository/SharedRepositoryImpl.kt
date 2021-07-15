package com.natife.testtask5.data.repository

import kotlinx.coroutines.flow.SharedFlow
import model.User
import javax.inject.Inject

class SharedRepositoryImpl @Inject constructor(
    private val connectToServer: ConnectServerRepository,
    private val workWithServer: WorkServerRepository
) : Repository {

//    val users = workWithServer.users

    override suspend fun connect(nickname: String) {
        connectToServer.sendPacket()
        connectToServer.stopSend()
        workWithServer.connectSocket(connectToServer.getIp(), nickname)
    }


    override fun fetchUsers() {
        workWithServer.fetchUsers()
    }

    override fun getUsers(): SharedFlow<List<User>> =
        workWithServer.users


}