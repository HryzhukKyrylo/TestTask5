package com.natife.testtask5.data.repository

import kotlinx.coroutines.flow.SharedFlow
import model.MessageItem
import model.Payload
import model.User

interface Repository {
    suspend fun connect(nickname: String)

    suspend fun fetchUsers()

    fun getUsers(): SharedFlow<List<User>>

    suspend fun getMessages(): SharedFlow<Payload>

    suspend fun sendMyMessage(idUser: String, message: String)
}
