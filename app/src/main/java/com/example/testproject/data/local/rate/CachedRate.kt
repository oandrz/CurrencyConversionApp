package com.example.testproject.data.local.rate

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
data class CachedRate (
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "rate_data_string") val rateData: String,
    @ColumnInfo(name = "timestamp") val createdAtTimeStamp: Long
)

