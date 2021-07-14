package com.natife.testtask5.ui.listusersscreen.viewmodel

import androidx.lifecycle.*
import com.natife.testtask5.data.repository.SharedRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import hilt_aggregated_deps._dagger_hilt_android_internal_managers_HiltWrapper_ActivityRetainedComponentManager_LifecycleModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.User
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: SharedRepositoryImpl
) : ViewModel() {


    private val _users = MutableLiveData<List<User>>()
    val users : LiveData<List<User>> = _users

     fun fetchUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchUsers()
            repository.getUsers(
        }

    }

}
