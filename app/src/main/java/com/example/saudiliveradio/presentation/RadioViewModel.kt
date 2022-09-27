package com.example.saudiliveradio.presentation

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.saudiliveradio.domain.repository.RadioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.viewModelScope
import com.example.saudiliveradio.domain.model.RadioData
import com.example.saudiliveradio.domain.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class RadioViewModel @Inject constructor(
     val repository: RadioRepository,

) : ViewModel() {


    private val _state = MutableLiveData<Map<Int, RadioData>>()
    val state: LiveData<Map<Int, RadioData>>
        get() = _state

    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean>
        get() = _isConnected

    private val _navigateToCheckConnection = MutableLiveData<Boolean?>()
    val navigateToCheckConnection: LiveData<Boolean?>
        get() = _navigateToCheckConnection

fun doneNavigtion(){
    _navigateToCheckConnection.value = null
}

    fun loadAllRadiosData(){

            viewModelScope.launch {
                val idRange: IntRange= 11 .. 15
                val content = mutableMapOf<Int , RadioData>()
                coroutineScope {
                    //Log.d(TAG, "loadAllRadiosData1: ${Thread.currentThread().name}")
                    idRange.forEach { id ->
                        launch{
                            //Log.d(TAG, "loadAllRadiosData: ${Thread.currentThread().name}")
                            when(val result = repository.getRadioData(id)){
                                is Resource.Success -> {
                                    content.put(id, result.data!!)

                                }
                                is Resource.Error -> {

                                }
                            }
                        }
                    }
                }
                // suspend fun loadAndCombine(name1: String, name2: String): Image =
                //    coroutineScope {
                //        val deferred1 = async { loadImage(name1) }
                //        val deferred2 = async { loadImage(name2) }
                //        combineImages(deferred1.await(), deferred2.await())
                //    }
                //You have to wrap your code into coroutineScope { … } block that establishes a boundary of your operation, its scope.
                // All the async coroutines become the children of this scope and, if the scope fails with an exception or is cancelled,
                // all the children are cancelled, too.
                _state.value =content
            }
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
                _isConnected.postValue(result)
            } catch (e: Exception) {
                //result = false
                _isConnected.postValue(result)
                _navigateToCheckConnection.postValue(true)
                e.printStackTrace()
            }
        }

    }
}
//saudi radio 11
//riyadhradio 12
//quranradio 13
//nidaalislam 14
//jeddahradio 15