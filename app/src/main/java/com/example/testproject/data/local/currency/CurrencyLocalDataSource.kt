package com.example.testproject.data.local.currency

import com.example.testproject.data.local.db.CurrencyDao
import javax.inject.Inject

class CurrencyLocalDataSource @Inject constructor(
    private val currencyDao: CurrencyDao
) {

    suspend fun getCurrencies(): List<CacheCurrency> = currencyDao.getAll()

    suspend fun saveCurrencies(param: List<CacheCurrency>) = currencyDao.insertAll(param)
}