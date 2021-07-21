package com.natife.testtask5.ui.chatscreen.viewmodel

import androidx.lifecycle.*
import com.natife.testtask5.data.model.MessageDto
import com.natife.testtask5.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: Repository,
    private val state: SavedStateHandle
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

//    private val user = state.

    fun sendMessage(idUser: String, message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.sendMyMessage(idUser, message)
        }
    }

    fun getId() = repository.getId()

}
