package com.example.saudiliveradio.di

import android.app.Application
import androidx.annotation.Nullable
import com.example.saudiliveradio.data.remote.RadioApi
import com.google.android.exoplayer2.ExoPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRadioApi(): RadioApi {

        return Retrofit.Builder()
            .baseUrl("https://aloula.faulio.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()

    }

    @Provides
    fun provideExoPlayer(App: Application): ExoPlayer {
        return  ExoPlayer
            .Builder(App)
            .build()
    }


}