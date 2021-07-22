package com.natife.testtask5.ui.chatscreen.viewmodel

import androidx.lifecycle.*
import com.natife.testtask5.data.model.MessageDto
import com.natife.testtask5.data.model.User
import com.natife.testtask5.data.repository.Repository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel @AssistedInject constructor(
    private val repository: Repository,
    @Assisted private val userArg: User?
) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getMessages().collect {
                withContext(Dispatchers.Main) {
                    messagesListener.value = it
                }
            }
        }
    }

    private val messagesListener = MutableLiveData<MessageDto>()
    val messages: LiveData<MessageDto> = messagesListener

    private val userListener = MutableLiveData<User?>(userArg)
    val user: LiveData<User?> = userListener

    fun sendMessage( message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.sendMyMessage(userArg?.id?:"", message)
        }
    }

    fun getId() = repository.getId()

    @dagger.assisted.AssistedFactory
    interface AssistedFactory{
        fun create (user:User?): ChatViewModel
    }

    companion object{
        fun provideFactory(
            assistedFactory: AssistedFactory,
            user: User?
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return  assistedFactory.create(user)  as T
            }
        }
    }

}
