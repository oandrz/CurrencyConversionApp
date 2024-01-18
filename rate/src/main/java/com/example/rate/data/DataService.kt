package com.example.rate.data

import com.example.rate.data.model.RatesResponse
import retrofit2.http.GET


interface DataService {
    @GET("33fc8459b4d4e2ed5a6ce2ffda5747bd/flightDeals/prices")
    suspend fun getRates(): RatesResponse
}

