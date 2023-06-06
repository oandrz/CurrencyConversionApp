package com.example.testproject.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.testproject.data.local.db.CacheCurrency
import com.example.testproject.data.local.db.CurrencyDao

@Database(
    entities = [CacheCurrency::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun CurrencyDao(): CurrencyDao
}