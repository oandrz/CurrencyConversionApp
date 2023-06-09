package com.example.testproject.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.testproject.data.local.currency.CacheCurrency
import com.example.testproject.data.local.rate.CachedRate
import com.example.testproject.data.local.rate.RateDao

@Database(
    entities = [CacheCurrency::class, CachedRate::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun CurrencyDao(): CurrencyDao

    abstract fun RateDao(): RateDao
}