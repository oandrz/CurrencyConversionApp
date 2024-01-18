package com.example.rate.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RateResponse(
    val city: String,
    val iataCode: String,
    val lowestPrice: Int,
    val id: Int
)