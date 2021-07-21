package com.natife.testtask5.data.repository

import com.natife.testtask5.data.model.MessageDto
import com.natife.testtask5.data.model.User
import com.natife.testtask5.util.CustomScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SharedRepositoryImpl @Inject constructor(
    private val connectRepository: ConnectServerRepository,
    private val serverRepository: ServerRepository
) : Repository {

    private val scope = CustomScope()
    private var cycleUsers = true

    override suspend fun connect(nickname: String) {
        connectRepository.sendPacket()
        serverRepository.connectSocket(connectRepository.getIp(), nickname)
    }

    override suspend fun startFetchUsers() {
        cycleUsers = true
        scope.launch {
            while (cycleUsers) {
                serverRepository.fetchUsers()
                delay(10000L)
            }
        }
    }

    override fun getUsers(): SharedFlow<List<User>> =
        serverRepository.getUsers()

    override suspend fun getMessages(): SharedFlow<MessageDto> =
        serverRepository.getMessages()

    override suspend fun sendMyMessage(idUser: String, message: String) {
        serverRepository.sendMyMessage(idUser, message)
    }

    override fun getId() = serverRepository.getId()

    override fun stopFetchUsers() {
        cycleUsers = false
    }

}
