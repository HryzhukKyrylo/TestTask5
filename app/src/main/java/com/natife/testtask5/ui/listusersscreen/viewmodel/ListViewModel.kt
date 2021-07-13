package com.natife.testtask5.ui.listusersscreen.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natife.testtask5.data.repository.ListRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import model.UsersReceivedDto
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val repository: ListRepositoryImpl) : ViewModel() {


    //    val _users = MutableLiveData<UsersReceivedDto>()
    val ip = MutableLiveData<String>()

    @RequiresApi(Build.VERSION_CODES.N)
    fun send() {
        viewModelScope.launch(Dispatchers.IO) {
            async { repository.connect() }
            async { repository.fetchUsers() }


        }
    }

}
