package com.example.testproject.di

import com.example.testproject.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Provides
    @Singleton
    fun provideCurrencyDao(db: AppDatabase) = db.CurrencyDao()

    @Provides
    @Singleton
    fun provideRateDao(db: AppDatabase) = db.RateDao()
}