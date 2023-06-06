package com.example.testproject.di

import com.example.testproject.data.network.currency.CurrencyService
import com.example.testproject.data.network.rate.RateService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideCurrencyService(retrofit: Retrofit): CurrencyService = retrofit.create(CurrencyService::class.java)

    @Provides
    @Singleton
    fun provideRateService(retrofit: Retrofit): RateService = retrofit.create(RateService::class.java)
}