package com.example.saudiliveradio.data.remote

import com.squareup.moshi.Json

data class RadioLogoDto(

    @field:Json(name = "small")
    val logo: String
)
