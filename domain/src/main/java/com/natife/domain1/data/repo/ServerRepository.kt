package com.natife.domain1.data.repo

import com.natife.domain1.data.model.MessageDto
import com.natife.domain1.data.model.User
import kotlinx.coroutines.flow.Flow

interface ServerRepository {

    suspend fun sendMyMessage(idUser: String, message: String)

    suspend fun connectSocket(ip: String, nickname: String)

    suspend fun startRequestingUsers()

    fun getUsers(): Flow<List<User>>

    fun getMessages(): Flow<MessageDto>

    fun getConnection(): Flow<Boolean>

    fun getId(): String

    suspend fun disconnectFromServer()

    fun stopRequestingUsers()
}
