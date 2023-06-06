package com.example.testproject.data.network.currency

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CurrencyRemoteDataSource @Inject constructor(
    private val currencyService: CurrencyService
) {

    suspend fun getCurrencies(): Map<String, String> = currencyService.getCurrencies()

}