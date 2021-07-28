package com.natife.testtask5.ui.loginscreen.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natife.domain1.data.repo.Repository
import com.natife.testtask5.util.PreferenceHelper.savedLogIn
import com.natife.testtask5.util.PreferenceHelper.savedNickname
import com.natife.testtask5.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: Repository,
    private val preferences: SharedPreferences
) : ViewModel() {

    private val mutableNavigate = SingleLiveEvent<Boolean>()
    val observeNavigate : SingleLiveEvent<Boolean> = mutableNavigate

    private val mutableNickname = MutableLiveData<String>()
    val observeNickname: LiveData<String> = mutableNickname

    private val mutableRememberNickname = MutableLiveData<Boolean>()
    val observeRememberNickname: LiveData<Boolean> = mutableRememberNickname

    fun connect(nickname: String) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main){
                mutableNavigate.call()
            }
            repository.connectToServer(nickname)
            mutableNavigate.postValue(true)
        }
    }

    fun initSettings() {
        if (preferences.savedLogIn) {
            mutableRememberNickname.value = true
            mutableNickname.value = preferences.savedNickname ?: ""
        }
    }

    fun saveNickname(name: String, checked: Boolean) {
        if (checked) {
            preferences.savedNickname = name
            preferences.savedLogIn = true
        } else {
            preferences.savedLogIn = false
        }
    }
}
