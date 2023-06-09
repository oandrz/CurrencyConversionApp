package com.example.testproject.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.testproject.data.local.currency.CacheCurrency

@Database(
    entities = [CacheCurrency::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun CurrencyDao(): CurrencyDao
}