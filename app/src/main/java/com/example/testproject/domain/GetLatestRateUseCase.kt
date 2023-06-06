package com.example.testproject.domain

import com.example.testproject.data.RateRepository
import com.example.testproject.domain.model.LatestRate
import com.example.testproject.ext.toResult
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetLatestRateUseCase @Inject constructor(
    private val rateRepository: RateRepository
) {
    operator fun invoke(): Flow<Result<LatestRate>> = rateRepository.getLatestRates().toResult()
}