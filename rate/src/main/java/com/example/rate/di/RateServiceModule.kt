package com.example.rate.di

import com.example.rate.data.DataService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object RateServiceModule {

    @Provides
    @ViewModelScoped
    fun provideService(retrofit: Retrofit): DataService = retrofit.create(DataService::class.java)
}