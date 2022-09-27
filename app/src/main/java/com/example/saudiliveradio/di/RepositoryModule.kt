package com.example.saudiliveradio.di

import com.example.saudiliveradio.data.repository.RadioRepositoryImpl
import com.example.saudiliveradio.domain.repository.RadioRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRadioRepository(radioRepositoryImpl: RadioRepositoryImpl) : RadioRepository

}