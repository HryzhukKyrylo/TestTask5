package com.natife.testtask5.data.repository

import kotlinx.coroutines.flow.SharedFlow
import model.MessageItem
import model.Payload
import model.User
import javax.inject.Inject

class SharedRepositoryImpl @Inject constructor(
    private val connectToServer: ConnectServerRepository,
    private val workWithServer: WorkServerRepository
) : Repository {

    override suspend fun connect(nickname: String) {
        connectToServer.sendPacket()
        connectToServer.stopSend()
        workWithServer.connectSocket(connectToServer.getIp(), nickname)
    }

    override suspend fun fetchUsers() {
        workWithServer.fetchUsers()
    }

    override fun getUsers(): SharedFlow<List<User>> =
        workWithServer.users

    override suspend fun getMessages(): SharedFlow<Payload> =
        workWithServer.messages

    override suspend fun sendMyMessage(idUser: String, message: String) {
        workWithServer.sendMyMessage(idUser, message)
    }
}
