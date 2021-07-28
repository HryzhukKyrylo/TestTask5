package com.natife.testtask5.ui.chatscreen.viewmodel

import androidx.lifecycle.*
import com.natife.domain1.data.model.MessageDto
import com.natife.domain1.data.repo.Repository
import com.natife.testtask5.util.SingleLiveEvent
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

class ChatViewModel @AssistedInject constructor(
    private val repository: Repository,
    @Assisted private val userArg: String?
) : ViewModel() {

    private val mutableMessage = MutableLiveData<MessageDto>()
    val observeMessage: LiveData<MessageDto> = mutableMessage

    private val mutableUser = MutableLiveData<String?>(userArg)
    val observeUser: LiveData<String?> = mutableUser

    private val mutableConnection = SingleLiveEvent<Boolean>()
    val observeConnection: SingleLiveEvent<Boolean> = mutableConnection

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getConnection().collectLatest {
                withContext(Dispatchers.Main) {
                    mutableConnection.value = it
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.getMessages().collectLatest {
                withContext(Dispatchers.Main) {
                    mutableMessage.value = it
                }
            }
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.sendMyMessage(userArg ?: "", message)
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
        fun create(user: String?): ChatViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            user: String?
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(user) as T
            }
        }
    }
}
