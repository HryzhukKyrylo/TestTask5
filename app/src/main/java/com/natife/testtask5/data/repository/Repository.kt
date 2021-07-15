package com.natife.testtask5.data.repository

import kotlinx.coroutines.flow.SharedFlow
import model.User

interface Repository {
    suspend fun connect(nickname: String)
    fun fetchUsers()
    fun getUsers(): SharedFlow<List<User>>

}