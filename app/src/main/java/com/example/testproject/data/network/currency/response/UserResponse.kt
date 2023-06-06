package com.example.testproject.data.network.currency.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    @SerialName("id") val userId: Int,
    @SerialName("username") val userName: String
)