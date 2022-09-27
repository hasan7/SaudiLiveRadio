package com.example.saudiliveradio.observeconnectivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.net.HttpURLConnection
import java.net.URL
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import javax.inject.Inject

@HiltViewModel
class CheckConnectionViewModel @Inject constructor() : ViewModel() {

    private val _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean>
        get() = _isSuccessful

    private val _navigateToRadio = MutableLiveData<Boolean?>()
    val navigateToRadio: LiveData<Boolean?>
        get() = _navigateToRadio

    fun doneNavigtion(){
        _navigateToRadio.value = null
    }

    fun  isReallyConnected() {

        val ip204 = "https://clients3.google.com/generate_204"
        var result = false
        viewModelScope.launch(Dispatchers.IO){

                    try {
                        val url = URL(ip204)
                        val urlc = url.openConnection() as HttpURLConnection
                        urlc.connectTimeout = 300
                        urlc.connect()
                         result = urlc.responseCode == 204
                        _isSuccessful.postValue(result)
                        _navigateToRadio.postValue(true)
                    } catch (e: Exception) {
                        //result = false
                        _isSuccessful.postValue(result)
                        e.printStackTrace()
                    }
        }

    }
}