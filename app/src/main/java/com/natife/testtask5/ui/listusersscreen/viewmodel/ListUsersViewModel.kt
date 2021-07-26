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

    private val mutableUsers = MutableLiveData<List<User>>()
    val observeUsers: LiveData<List<User>> = mutableUsers

    private val mutableConnection = MutableLiveData<Boolean>()
    val observeConnection: LiveData<Boolean> = mutableConnection

    init {
        viewModelScope.launch(Dispatchers.IO) {
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

    fun startRequestingUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.startRequestingUsers()
        }
    }

    fun stopRequestingUsers() {
        repository.stopRequestingUsers()
    }

    fun reconnectToServer() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.reconnectToServer()
            repository.startRequestingUsers()
        }
    }

    fun disconnectToServer() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.disconnectToServer()
        }
    }

}
