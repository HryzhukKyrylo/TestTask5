package com.natife.testtask5.ui.chatscreen.viewmodel

import androidx.lifecycle.*
import com.natife.core.data.model.MessageDto
import com.natife.core.data.model.User
import com.natife.core.data.repository.Repository
import com.natife.testtask5.util.CustomScope
import com.natife.testtask5.util.SingleLiveEvent
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.*

class ChatViewModel @AssistedInject constructor(
    private val repository: Repository,
    @Assisted private val userArg: User?
) : ViewModel() {
    private val coroutineScope = CustomScope()


    private val mutableMessage = MutableLiveData<MessageDto>()
    val observeMessage: LiveData<MessageDto> = mutableMessage

    private val mutableUser = MutableLiveData<User?>(userArg)
    val observeUser: LiveData<User?> = mutableUser

    private val mutableConnection = SingleLiveEvent<Boolean>()
    val observeConnection: SingleLiveEvent<Boolean> = mutableConnection

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getConnection().collect {
                withContext(Dispatchers.Main) {
                    mutableConnection.value = it
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.getMessages().collect {
                withContext(Dispatchers.Main) {
                    mutableMessage.value = it
                }
            }
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.sendMyMessage(userArg?.id ?: "", message)
        }
    }

    fun getId() = repository.getId()

    fun reconnectToServer() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.reconnectToServer()
        }
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
