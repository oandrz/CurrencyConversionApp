package com.example.testproject.data.local.rate

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RateDao {

    @Query("SELECT * FROM CachedRate ORDER BY timestamp DESC ")
    suspend fun getCachedRate(): List<CachedRate>

    @Insert
    suspend fun insert(rate: CachedRate)
}