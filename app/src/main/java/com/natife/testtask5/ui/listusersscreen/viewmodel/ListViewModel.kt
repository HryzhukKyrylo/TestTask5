package com.natife.testtask5.ui.listusersscreen.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natife.testtask5.data.repository.ListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor( private val repository : ListRepository): ViewModel() {


    val ip  = MutableLiveData<String>()

    fun send(){
        viewModelScope.launch(Dispatchers.IO) {

               ip.postValue(repository.sendPacket())
        }
    }

}
