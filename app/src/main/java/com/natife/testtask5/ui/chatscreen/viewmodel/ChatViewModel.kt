package com.natife.testtask5.ui.chatscreen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natife.testtask5.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.Payload
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getMessages().collect {
                withContext(Dispatchers.Main) {
                    _messages.value = it
                }
            }
        }
    }

    private val _messages = MutableLiveData<Payload>()
    val messages: LiveData<Payload> = _messages

    fun sendMessage(idUser: String, message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.sendMyMessage(idUser, message)
        }
    }

}
