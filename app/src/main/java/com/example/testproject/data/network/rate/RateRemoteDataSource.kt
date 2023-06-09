package com.example.testproject.data.network.rate

import com.example.testproject.data.network.rate.response.LatestRateResponse
import javax.inject.Inject

class RateRemoteDataSource @Inject constructor(
    private val rateService: RateService
) {

    suspend fun getLatestRate(): LatestRateResponse =
        rateService.getLatestRate()
}