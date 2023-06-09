package com.example.testproject.data.local.currency

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CacheCurrency(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "currency_symbol") val currencySymbol: String,
    @ColumnInfo(name = "currency") val currency: String
)