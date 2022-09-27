package com.example.saudiliveradio.data.remote

import com.squareup.moshi.Json

data class RadioDto(

    @field:Json(name = "logo")
    val logoUrl: RadioLogoDto,

    @field:Json(name = "streams")
    val streamUrl: RadioStreamDto


)
