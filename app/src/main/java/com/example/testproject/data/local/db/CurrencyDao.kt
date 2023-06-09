package com.example.testproject.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.testproject.data.local.currency.CacheCurrency

@Dao
interface CurrencyDao {

    @Query("select * from CacheCurrency")
    suspend fun getAll(): List<CacheCurrency>

    @Insert
    suspend fun insertAll(currencies: List<CacheCurrency>)
}