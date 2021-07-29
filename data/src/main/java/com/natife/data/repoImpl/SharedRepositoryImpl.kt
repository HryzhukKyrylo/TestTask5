package com.natife.data.repoImpl

import com.natife.domain1.data.model.MessageDto
import com.natife.domain1.data.model.User
import com.natife.domain1.data.repo.ConnectServerRepository
import com.natife.domain1.data.repo.Repository
import com.natife.domain1.data.repo.ServerRepository
import kotlinx.coroutines.flow.Flow
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

    override fun getUsers(): Flow<List<User>> =
        serverRepository.getUsers()

    override suspend fun getMessages(): Flow<MessageDto> =
        serverRepository.getMessages()

    override suspend fun getConnection(): Flow<Boolean> =
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

    override suspend fun disconnectFromServer() {
        serverRepository.disconnectFromServer()
    }

}
