package com.natife.testtask5.ui.loginscreen.viewmodel

import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natife.testtask5.data.repository.Repository
import com.natife.testtask5.util.PreferenceHelper.checkLogin
import com.natife.testtask5.util.PreferenceHelper.savedNickname
import com.natife.testtask5.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: Repository,
    private val preferences: SharedPreferences
) : ViewModel() {

    private val mutableNavigate = SingleLiveEvent<Boolean>()
    val navigate : SingleLiveEvent<Boolean> = mutableNavigate

    private val preferencesSaveNickname = MutableLiveData<String>()
    val savedNickname: LiveData<String> = preferencesSaveNickname

    private val preferencesRememberNickname = MutableLiveData<Boolean>()
    val rememberNickname: LiveData<Boolean> = preferencesRememberNickname

    fun connect(nickname: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mutableNavigate.postValue(false)
            repository.connect(nickname)
            mutableNavigate.postValue(true)
        }
    }

    fun initSettings() {
        if (preferences.checkLogin) {
            preferencesRememberNickname.value = true
            preferencesSaveNickname.value = preferences.savedNickname ?: ""
        }
    }

    fun forget() {
        mutableNavigate.postValue(false)
    }

    fun saveNickname(name: String, checked: Boolean) {
        if (checked) {
            preferences.savedNickname = name
            preferences.checkLogin = true
        } else {
            preferences.checkLogin = false
        }
    }
}
