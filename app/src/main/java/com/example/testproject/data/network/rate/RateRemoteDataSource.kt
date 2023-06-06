package com.example.testproject.data.network.rate

import com.example.testproject.data.network.rate.response.LatestRateResponse
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RateRemoteDataSource @Inject constructor(
    private val rateService: RateService
) {

    fun getLatestRate(): Flow<LatestRateResponse> = flow {
        emit(rateService.getLatestRate())
    }
}