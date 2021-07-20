package com.natife.testtask5.data.repository

import com.natife.testtask5.data.model.MessageDto
import com.natife.testtask5.data.model.Payload
import kotlinx.coroutines.flow.SharedFlow
import com.natife.testtask5.data.model.User
import javax.inject.Inject

class SharedRepositoryImpl @Inject constructor(
    private val connectRepository: ConnectServerRepository,
    private val serverRepository: ServerRepository
) : Repository {

    override suspend fun connect(nickname: String) {
        connectRepository.sendPacket()
        connectRepository.stopSend()
        serverRepository.connectSocket(connectRepository.getIp(), nickname)
    }

    override suspend fun fetchUsers() {
        serverRepository.fetchUsers()
    }

    override fun getUsers(): SharedFlow<List<User>> =
        serverRepository.getUsers()

    override suspend fun getMessages(): SharedFlow<MessageDto> =
        serverRepository.getMessages()

    override suspend fun sendMyMessage(idUser: String, message: String) {
        serverRepository.sendMyMessage(idUser, message)
    }

    override fun getId() = serverRepository.getId()

}
