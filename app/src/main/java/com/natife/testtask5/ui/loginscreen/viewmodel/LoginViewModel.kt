package com.natife.testtask5.ui.loginscreen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natife.testtask5.data.repository.Repository
import com.natife.testtask5.data.repository.SharedRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {

    private val _navigate = MutableLiveData<Boolean>()
    val navigate: LiveData<Boolean> = _navigate


    fun connect(nickname: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _navigate.postValue(false)
          repository.connect(nickname)
//            delay(2000L)
            _navigate.postValue(true)

        }
    }

    fun forget() {
        _navigate.postValue(false)
    }

}
