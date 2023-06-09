package com.example.testproject.data.network.rate

import com.example.testproject.data.network.rate.response.LatestRateResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RateService {
    @GET("latest.json")
    suspend fun getLatestRate(): LatestRateResponse
}