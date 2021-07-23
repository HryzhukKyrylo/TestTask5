package com.natife.testtask5.ui.chatscreen.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.natife.testtask5.data.model.MessageDto
import com.natife.testtask5.data.model.User
import com.natife.testtask5.data.repository.Repository
import com.natife.testtask5.util.CustomScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class ChatViewModel @AssistedInject constructor(
    private val repository: Repository,
    @Assisted private val userArg: User?
) : ViewModel() {
    private val coroutineScope = CustomScope()

    init {
        coroutineScope.launch {
            repository.getConnection().collect {
                withContext(Dispatchers.Main) {
                    mutableConnection.value = it
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.getMessages().collect {
                withContext(Dispatchers.Main) {
                    messagesListener.value = it
                }
            }
        }
        Log.i("TAG", "ChatViewModel/init: initialized")
    }

    private val messagesListener = MutableLiveData<MessageDto>()
    val messages: LiveData<MessageDto> = messagesListener

    private val userListener = MutableLiveData<User?>(userArg)
    val user: LiveData<User?> = userListener

    private val mutableConnection = MutableLiveData<Boolean>()
    val connection: LiveData<Boolean> = mutableConnection

    fun sendMessage(message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.sendMyMessage(userArg?.id ?: "", message)
        }
    }

    fun getId() = repository.getId()

    fun reconnect() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.reconnect()
        }
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancelChildren()
    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(user: User?): ChatViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            user: User?
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(user) as T
            }
        }
    }

}
