package com.example.testproject.data.network.currency

import retrofit2.http.GET

interface CurrencyService {
    @GET("currencies.json")
    suspend fun getCurrencies(): Map<String, String>
}