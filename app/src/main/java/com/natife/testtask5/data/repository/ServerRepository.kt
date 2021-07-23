package com.natife.testtask5.data.repository

import com.natife.testtask5.data.model.MessageDto
import com.natife.testtask5.data.model.User
import kotlinx.coroutines.flow.SharedFlow

interface ServerRepository {

    suspend fun sendMyMessage(idUser: String, message: String)

    suspend fun connectSocket(ip: String, nickname: String)

    suspend fun startRequestingUsers()

    fun getUsers(): SharedFlow<List<User>>

    fun getMessages(): SharedFlow<MessageDto>

    fun getConnection(): SharedFlow<Boolean>

    fun getId(): String

    suspend fun disconnectToServer()

    fun stopRequestingUsers()
}
