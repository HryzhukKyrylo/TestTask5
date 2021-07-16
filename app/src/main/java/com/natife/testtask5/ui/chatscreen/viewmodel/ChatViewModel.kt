package com.natife.testtask5.ui.chatscreen.viewmodel

import android.util.Log
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
        Log.i("TAG", "INIT VIEWMODEL------")

        viewModelScope.launch(Dispatchers.IO) {
            repository.getMessages().collect {
                Log.i("TAG", "COLLECT------: $it")
                withContext(Dispatchers.Main) {
                    _messages.value = it
                }
            }
//            repository.getMessages2().collect {
//                withContext(Dispatchers.Main) {
//                    _messages2.emit(it)
//                }
//            }
        }
    }

    private val _messages = MutableLiveData<Payload>()
    val messages: LiveData<Payload> = _messages

//    private val _messages2 = MutableLiveData<String>()
//    val messages2: LiveData<String> = _messages2
//    private val _messages2 = MutableSharedFlow<String>(
//        replay = 1,
//        extraBufferCapacity = 10,
//        onBufferOverflow = BufferOverflow.DROP_OLDEST
//    )
//    val messages2: SharedFlow<String> = _messages2

    fun sendMessage(idUser: String, message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.sendMyMessage(idUser, message)
        }
    }

}
