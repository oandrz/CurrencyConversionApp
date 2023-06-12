package com.example.testproject.di

import android.content.Context
import androidx.room.Room
import com.example.testproject.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, AppDatabase::class.java, name = "currency-db")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
}