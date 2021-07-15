package com.natife.testtask5.ui.chatscreen.viewmodel

import androidx.lifecycle.ViewModel
import com.natife.testtask5.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: Repository
) :ViewModel() {

}