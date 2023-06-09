package com.example.testproject.data

import com.example.testproject.data.local.rate.CachedRate
import com.example.testproject.data.local.rate.RateLocalDataSource
import com.example.testproject.data.network.rate.LatestRateResponseTransformer
import com.example.testproject.data.network.rate.RateRemoteDataSource
import com.example.testproject.data.network.rate.response.LatestRateResponse
import com.example.testproject.domain.model.LatestRate
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

interface RateRepository {
    fun getLatestRates(): Flow<LatestRate>
}

class RateRepositoryImpl @Inject constructor(
    private val rateRemoteDataSource: RateRemoteDataSource,
    private val rateLocalDataSource: RateLocalDataSource,
    private val rateResponseTransformer: LatestRateResponseTransformer
) : RateRepository {

    override fun getLatestRates(): Flow<LatestRate> = flow {
        val cachedRate = rateLocalDataSource.getCachedRate().firstOrNull()
        emit(
            if (cachedRate != null) {
                getDataFromCache(cachedRate)
            } else {
                refreshRate()
            }
        )
    }

    private suspend fun getDataFromCache(cachedRate: CachedRate): LatestRate {
        val deltaMillisInMinute = (System.currentTimeMillis() - cachedRate.createdAtTimeStamp) / 60000
        val needRefresh = deltaMillisInMinute >= THIRTY_MINUTE
        return if (needRefresh) {
            refreshRate()
        } else {
            val rateResponse: LatestRateResponse = Json.decodeFromString(cachedRate.rateData)
            rateResponseTransformer(rateResponse)
        }
    }

    private suspend fun refreshRate(): LatestRate {
        val response = rateRemoteDataSource.getLatestRate()

        rateLocalDataSource.saveRate(
            CachedRate(
                rateData = Json.encodeToString(response),
                createdAtTimeStamp = response.timestamp * 1000
            )
        )

        return rateResponseTransformer(response)
    }

    companion object {
        private const val THIRTY_MINUTE = 30
    }
}