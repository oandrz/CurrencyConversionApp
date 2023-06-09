package com.example.testproject.data

import com.example.testproject.data.local.currency.CurrencyLocalDataSource
import com.example.testproject.data.local.currency.CacheCurrency
import com.example.testproject.data.network.currency.CurrencyRemoteDataSource
import com.example.testproject.domain.model.CurrencyDetail
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface CurrencyRepository {
    fun getCurrencies(): Flow<List<CurrencyDetail>>
}

class CurrencyRepositoryImpl @Inject constructor(
    private val localDataSource: CurrencyLocalDataSource,
    private val remoteDataStore: CurrencyRemoteDataSource
) : CurrencyRepository {

    override fun getCurrencies(): Flow<List<CurrencyDetail>> = flow {
        val cachedCurrency = localDataSource.getCurrencies()
        emit(
            if (cachedCurrency.isEmpty()) {
                refreshCurrency()
            } else {
                cachedCurrency.map { CurrencyDetail(it.currencySymbol, it.currency) }
            }
        )
    }

    private suspend fun refreshCurrency(): List<CurrencyDetail> {
        val currencies = remoteDataStore.getCurrencies().map {
            CurrencyDetail(
                currencySymbol = it.key,
                currency = it.value
            )
        }
        localDataSource.saveCurrencies(
            currencies.map { CacheCurrency(currencySymbol =  it.currencySymbol, currency = it.currency) }
        )

        return currencies
    }
}