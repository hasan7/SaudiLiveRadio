package com.example.saudiliveradio.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RadioApi {

    @GET("api/v1/channels/{id}")
    suspend fun getRadioData(
        @Path("id") channelId: Int
    ) : RadioDto

}