package com.example.testproject.data.network.currency

import javax.inject.Inject

class CurrencyRemoteDataSource @Inject constructor(
    private val currencyService: CurrencyService
) {

    suspend fun getCurrencies(): Map<String, String> = currencyService.getCurrencies()
}