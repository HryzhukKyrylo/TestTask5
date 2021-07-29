package com.natife.testtask5.ui.listusersscreen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natife.domain1.data.model.User
import com.natife.domain1.data.repo.Repository
import com.natife.testtask5.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListUsersViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val mutableUsers = MutableLiveData<List<User>>()
    val observeUsers: LiveData<List<User>> = mutableUsers

    private val mutableConnection = SingleLiveEvent<Boolean>()
    val observeConnection: SingleLiveEvent<Boolean> = mutableConnection

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getConnection().collectLatest() {
                withContext(Dispatchers.Main) {
                    mutableConnection.value = it
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUsers().collectLatest {
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

    fun disconnectFromServer() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.disconnectFromServer()
        }
    }

}
