package com.natife.domain1.data.repo

import com.natife.domain1.data.model.MessageDto
import com.natife.domain1.data.model.User
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun connectToServer(nickname: String)

    suspend fun startRequestingUsers()

    fun getUsers(): Flow<List<User>>

    suspend fun getMessages(): Flow<MessageDto>

    suspend fun getConnection(): Flow<Boolean>

    suspend fun sendMyMessage(idUser: String, message: String)

    fun getId(): String

    fun stopRequestingUsers()

    suspend fun reconnectToServer()

    suspend fun disconnectFromServer()
}
