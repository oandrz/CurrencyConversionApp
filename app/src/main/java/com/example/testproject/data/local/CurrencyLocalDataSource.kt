package com.example.testproject.data.local

import com.example.testproject.data.local.db.CacheCurrency
import com.example.testproject.data.local.db.CurrencyDao
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CurrencyLocalDataSource @Inject constructor(
    private val currencyDao: CurrencyDao
) {

    suspend fun getCurrencies(): List<CacheCurrency> = currencyDao.getAll()

    suspend fun saveCurrencies(param: List<CacheCurrency>) = currencyDao.insertAll(param)
}