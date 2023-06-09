package com.example.testproject.data.local.currency

import com.example.testproject.data.local.db.CurrencyDao
import javax.inject.Inject

class CurrencyLocalDataSource @Inject constructor(
    private val currencyDao: CurrencyDao
) {

    suspend fun getCurrencies(): List<CacheCurrency> = currencyDao.getCurrencyDict()

    suspend fun saveCurrencies(cache: CacheCurrency) = currencyDao.insert(cache)
}