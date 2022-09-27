package com.example.saudiliveradio.domain.repository

import com.example.saudiliveradio.domain.model.RadioData
import com.example.saudiliveradio.domain.util.Resource

interface RadioRepository {

    suspend fun getRadioData(id: Int) : Resource<RadioData>

}