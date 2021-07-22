package com.natife.testtask5.data.repository

import com.natife.testtask5.data.model.MessageDto
import com.natife.testtask5.data.model.User
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class SharedRepositoryImpl @Inject constructor(
    private val connectRepository: ConnectServerRepository,
    private val serverRepository: ServerRepository
) : Repository {

    private var nickname = ""
    private var ip = ""

    override suspend fun connect(nickname: String) {
        this.nickname = nickname
        ip = connectRepository.sendPacket()
        serverRepository.connectSocket(ip, nickname)
    }

    override suspend fun startFetchUsers() {
        serverRepository.fetchUsers()
    }

    override fun getUsers(): SharedFlow<List<User>> =
        serverRepository.getUsers()

    override suspend fun getMessages(): SharedFlow<MessageDto> =
        serverRepository.getMessages()

    override suspend fun getConnection(): SharedFlow<Boolean> =
        serverRepository.getConnection()

    override suspend fun sendMyMessage(idUser: String, message: String) {
        serverRepository.sendMyMessage(idUser, message)
    }

    override fun getId() = serverRepository.getId()

    override fun stopFetchUsers() {
        serverRepository.stopFetchUsers()
    }

    override suspend fun reconnect() {
        connect(nickname)
    }

    override suspend fun disconnect() {
        serverRepository.disconnect()
    }

}
