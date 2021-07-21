package com.natife.testtask5.ui.listusersscreen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natife.testtask5.data.model.User
import com.natife.testtask5.data.repository.Repository
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

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUsers().collect {
                withContext(Dispatchers.Main) {
                    usersListeners.value = it
                }
            }
        }
    }

    private val usersListeners = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = usersListeners

    fun fetchUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.startFetchUsers()
        }
    }

    fun stopFetchUsers() {
        repository.stopFetchUsers()
    }

    fun disconnect() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.disconnect()
        }
    }

}
