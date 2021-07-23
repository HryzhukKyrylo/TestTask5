package com.natife.testtask5.ui.listusersscreen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natife.testtask5.data.model.User
import com.natife.testtask5.data.repository.Repository
import com.natife.testtask5.util.CustomScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListUsersViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val coroutineScope = CustomScope()

    init {
        coroutineScope.launch {
            repository.getConnection().collect {
                withContext(Dispatchers.Main) {
                    mutableConnection.value = it
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUsers().collect {
                withContext(Dispatchers.Main) {
                    mutableUsers.value = it
                }
            }
        }
    }

    private val mutableUsers = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = mutableUsers

    private val mutableConnection = MutableLiveData<Boolean>()
    val connection: LiveData<Boolean> = mutableConnection


    fun fetchUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.startFetchUsers()
        }
    }

    fun stopFetchUsers() {
        repository.stopFetchUsers()
    }

    fun reconnect() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.reconnect()
            repository.startFetchUsers()
        }
    }

    fun disconnect() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.disconnect()
        }
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancelChildren()
    }
}
