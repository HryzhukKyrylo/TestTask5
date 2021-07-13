package com.natife.testtask5.ui.listusersscreen.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natife.testtask5.data.repository.ListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val repository: ListRepository) : ViewModel() {


    val ip = MutableLiveData<String>()

    fun send() {
        viewModelScope.launch(Dispatchers.IO) {
           repository.connect()
        }
    }

}
