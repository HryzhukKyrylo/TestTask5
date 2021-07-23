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

    override suspend fun connectToServer(nickname: String) {
        this.nickname = nickname
        val ip = connectRepository.requestIp()
        serverRepository.connectSocket(ip, nickname)
    }

    override suspend fun startRequestingUsers() {
        serverRepository.startRequestingUsers()
    }

    override fun getUsers(): SharedFlow<List<User>> =
        serverRepository.getUsers()

    override suspend fun getMessages(): SharedFlow<MessageDto> =
        serverRepository.getMessages()

    override suspend fun getConnection(): SharedFlow<Boolean> =
        serverRepository.getConnection()

    override fun getId() = serverRepository.getId()

    override suspend fun sendMyMessage(idUser: String, message: String) {
        serverRepository.sendMyMessage(idUser, message)
    }

    override fun stopRequestingUsers() {
        serverRepository.stopRequestingUsers()
    }

    override suspend fun reconnectToServer() {
        connectToServer(nickname)
    }

    override suspend fun disconnectToServer() {
        serverRepository.disconnectToServer()
    }

}
