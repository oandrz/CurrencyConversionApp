package com.example.testproject.data.network.rate.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LatestRateResponse(
    @SerialName("timestamp") val timestamp: Long,
    @SerialName("base") val baseCurrency: String,
    @SerialName("rates") val rates: Map<String, Double>
)