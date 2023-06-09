package com.example.testproject.data.network.rate

import com.example.testproject.data.network.rate.response.LatestRateResponse
import com.example.testproject.domain.model.CurrencyRate
import com.example.testproject.domain.model.LatestRate
import javax.inject.Inject

class LatestRateResponseTransformer @Inject constructor() {

    operator fun invoke(response: LatestRateResponse) = LatestRate(
        baseRate = CurrencyRate(rate = 1.0, response.baseCurrency),
        rates = response.rates.map { (currency, rate) ->
            CurrencyRate(currency = currency, rate = rate)
        }
    )
}