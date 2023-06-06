package com.example.testproject.data.network.rate

import com.example.testproject.data.network.rate.response.LatestRateResponse
import retrofit2.http.GET

interface RateService {
    @GET("latest.json")
    suspend fun getLatestRate(): LatestRateResponse
}