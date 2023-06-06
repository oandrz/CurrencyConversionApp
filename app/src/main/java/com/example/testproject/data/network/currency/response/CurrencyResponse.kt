package com.example.testproject.data.network.currency.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyResponse(
    @SerialName("albumId") val albumId: Int,
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("url") val imageUrl: String,
    @SerialName("thumbnailUrl") val thumbnailUrl: String,
)