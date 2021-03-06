package com.natife.testtask5.data.repository

import com.natife.testtask5.data.model.MessageDto
import kotlinx.coroutines.flow.SharedFlow
import com.natife.testtask5.data.model.User

interface Repository {

    suspend fun connectToServer(nickname: String)

    suspend fun startRequestingUsers()

    fun getUsers(): SharedFlow<List<User>>

    suspend fun getMessages(): SharedFlow<MessageDto>

    suspend fun getConnection(): SharedFlow<Boolean>

    suspend fun sendMyMessage(idUser: String, message: String)

    fun getId(): String

    fun stopRequestingUsers()

    suspend fun reconnectToServer()

    suspend fun disconnectFromServer()
}
