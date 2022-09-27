package com.example.saudiliveradio.data.remote

import com.squareup.moshi.Json

data class RadioStreamDto(

    @field:Json(name = "hls")
    val streamUrl: String

)
