package com.natife.testtask5.data.repository

import com.natife.testtask5.data.model.MessageDto
import kotlinx.coroutines.flow.SharedFlow
import com.natife.testtask5.data.model.User

interface Repository {

    suspend fun connect(nickname: String)

    suspend fun startFetchUsers()

    fun getUsers(): SharedFlow<List<User>>

    suspend fun getMessages(): SharedFlow<MessageDto>

    suspend fun getConnection(): SharedFlow<Boolean>

    suspend fun sendMyMessage(idUser: String, message: String)

    fun getId(): String

    fun stopFetchUsers()

    suspend fun reconnect()

    suspend fun disconnect()
}
