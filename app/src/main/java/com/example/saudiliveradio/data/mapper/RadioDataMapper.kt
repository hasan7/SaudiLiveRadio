package com.example.saudiliveradio.data.mapper

import com.example.saudiliveradio.data.remote.RadioDto
import com.example.saudiliveradio.data.remote.RadioLogoDto
import com.example.saudiliveradio.data.remote.RadioStreamDto
import com.example.saudiliveradio.domain.model.RadioData

fun RadioLogoDto.toRadioLogoData(): String {

    return logo

}

fun RadioStreamDto.toRadioStreamData(): String {

    return streamUrl

}

fun RadioDto.toRadioData(): RadioData {

    return RadioData(
        radioLogo = logoUrl.toRadioLogoData(),
        radioStream = streamUrl.toRadioStreamData()
    )

}