package com.example.rate.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RatesResponse(@SerialName("prices") val prices: List<RateResponse>, )