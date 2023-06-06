package com.example.testproject.data

import com.example.testproject.data.network.rate.RateRemoteDataSource
import com.example.testproject.domain.model.CurrencyRate
import com.example.testproject.domain.model.LatestRate
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface RateRepository {
    fun getLatestRates(): Flow<LatestRate>
}

class RateRepositoryImpl @Inject constructor(
    private val rateRemoteDataSource: RateRemoteDataSource
) : RateRepository {

    override fun getLatestRates() =
        rateRemoteDataSource.getLatestRate().map {
            LatestRate(
                CurrencyRate(rate = 1.0, it.baseCurrency),
                it.rates.map { (currency, rate) ->
                    CurrencyRate(currency = currency, rate = rate)
                }
            )
        }
}