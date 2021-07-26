package com.natife.core.data.repository

import com.natife.core.data.model.MessageDto
import com.natife.core.data.model.User
import kotlinx.coroutines.flow.SharedFlow

interface ServerRepository {

    suspend fun sendMyMessage(idUser: String, message: String)

    suspend fun connectSocket(ip: String, nickname: String)

    suspend fun startRequestingUsers()

    fun getUsers(): SharedFlow<List<User>>

    fun getMessages(): SharedFlow<MessageDto>

    fun getConnection(): SharedFlow<Boolean>

    fun getId(): String

    suspend fun disconnectFromServer()

    fun stopRequestingUsers()
}
