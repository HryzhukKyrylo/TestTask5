package com.natife.testtask5.ui.sharedviewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natife.testtask5.data.repository.SharedRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *@author admin
 */
@HiltViewModel
class SharedViewModel @Inject constructor(private val repository: SharedRepositoryImpl) :
    ViewModel() {
//
//    val navigate = MutableLiveData<Boolean>()
//    val id = ""
//    var name = ""
//
//    @RequiresApi(Build.VERSION_CODES.N)
//    fun connect(nickname: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            navigate.postValue(false)
//            val connected = async { repository.connect(nickname) }
//            if (connected.await()) {
//                navigate.postValue(true)
//            }
//
//        }
//    }
//
//    fun forget() {
//        navigate.postValue(false)
//    }
}
