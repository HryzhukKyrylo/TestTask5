package com.natife.testtask5.data.repository

import com.natife.testtask5.data.model.MessageDto
import com.natife.testtask5.data.model.User
import kotlinx.coroutines.flow.SharedFlow

interface ServerRepository {

    suspend fun sendMyMessage(idUser: String, message: String)

    fun connectSocket(ip: String, nickname: String)

    suspend fun fetchUsers()

    fun getUsers(): SharedFlow<List<User>>

    fun getMessages(): SharedFlow<MessageDto>

    fun getConnection(): SharedFlow<Boolean>

    fun getId(): String

    suspend fun disconnect()

    fun stopFetchUsers()
}
