package com.example.saudiliveradio.presentation

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RadioPlayerViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle
) : ViewModel() {

    //val args = savedStateHandle.get<String>("radio_url")
    //theres two ways to get the args from the savedStateHandle, the secound one by using type safe args
    val args = RadioPlayerFragmentArgs.fromSavedStateHandle(savedStateHandle)

    private val _navigateToCheckConnection = MutableLiveData<Boolean?>()
    val navigateToCheckConnection: LiveData<Boolean?>
        get() = _navigateToCheckConnection

    fun doneNavigtion(){
        _navigateToCheckConnection.value = null
    }

    fun navigateToCheckConnection(){
        _navigateToCheckConnection.value = true
    }




}