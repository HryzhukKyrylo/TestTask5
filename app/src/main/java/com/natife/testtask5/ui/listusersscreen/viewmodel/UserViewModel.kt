package com.natife.testtask5.ui.listusersscreen.viewmodel

import androidx.lifecycle.*
import com.natife.testtask5.data.repository.SharedRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.User
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: SharedRepositoryImpl
) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.users.collect {
                withContext(Dispatchers.Main) {
                    _users.value = it
                }
            }
        }
    }

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    fun fetchUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchUsers()
        }
    }

}



