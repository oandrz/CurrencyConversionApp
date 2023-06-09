package com.example.testproject.data.local.rate

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CachedRate (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "rate_data_string") val rateData: String,
    @ColumnInfo(name = "created_at") val createdAtTimeStamp: Long
)

