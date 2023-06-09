package com.example.testproject.data.local.rate

import javax.inject.Inject

class RateLocalDataSource @Inject constructor(
    private val rateDao: RateDao
) {

    suspend fun getCachedRate(): List<CachedRate> = rateDao.getCachedRate()

    suspend fun saveRate(rate: CachedRate) = rateDao.insert(rate)
}