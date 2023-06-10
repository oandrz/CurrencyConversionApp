package com.example.testproject.data

import com.example.testproject.data.local.currency.CacheCurrency
import com.example.testproject.data.local.currency.CurrencyLocalDataSource
import com.example.testproject.data.network.currency.CurrencyRemoteDataSource
import com.example.testproject.domain.model.CurrencyDetail
import com.example.testproject.ext.findDifferenceWithCurrentTimeInMinute
import com.example.testproject.util.THIRTY_MINUTE
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

interface CurrencyRepository {
    fun getCurrencies(): Flow<List<CurrencyDetail>>
}

class CurrencyRepositoryImpl @Inject constructor(
    private val localDataSource: CurrencyLocalDataSource,
    private val remoteDataSource: CurrencyRemoteDataSource
) : CurrencyRepository {

    override fun getCurrencies(): Flow<List<CurrencyDetail>> = flow {
        val cachedCurrency = localDataSource.getCurrencies().firstOrNull()
        emit(
            if (cachedCurrency != null) {
                getDataFromCache(cachedCurrency)
            } else {
                refreshCurrency()
            }
        )
    }

    private suspend fun getDataFromCache(cachedCurrency: CacheCurrency): List<CurrencyDetail> {
        val cachedTime = cachedCurrency.timestamp.findDifferenceWithCurrentTimeInMinute()
        val isNeedRefresh = cachedTime >= THIRTY_MINUTE
        return if (isNeedRefresh) {
            refreshCurrency()
        } else {
            val response = Json.decodeFromString<Map<String, String>>(cachedCurrency.currencyDictionary)
            response.map { (currencySymbol, currencyDetail) ->
                CurrencyDetail(currencySymbol, currencyDetail)
            }
        }
    }

    private suspend fun refreshCurrency(): List<CurrencyDetail> {
        val response = remoteDataSource.getCurrencies()
        localDataSource.saveCurrencies(
            CacheCurrency(
                currencyDictionary = Json.encodeToString(response),
                timestamp = System.currentTimeMillis()
            )
        )
        return response.map {
            CurrencyDetail(
                currencySymbol = it.key,
                currency = it.value
            )
        }
    }
}