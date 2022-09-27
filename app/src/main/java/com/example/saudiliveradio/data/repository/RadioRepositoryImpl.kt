package com.example.saudiliveradio.data.repository

import com.example.saudiliveradio.data.mapper.toRadioData
import com.example.saudiliveradio.data.remote.RadioApi
import com.example.saudiliveradio.domain.model.RadioData
import com.example.saudiliveradio.domain.repository.RadioRepository
import com.example.saudiliveradio.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RadioRepositoryImpl @Inject constructor(
    private val api: RadioApi
) : RadioRepository {

    override suspend fun getRadioData(id: Int): Resource<RadioData> {

        return try {
            Resource.Success(
                withContext(Dispatchers.IO){
                    api.getRadioData(id).toRadioData()
                }

            )
        } catch(e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }



    }
}