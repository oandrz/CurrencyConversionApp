package com.example.testproject.presentation.rate.model

import java.math.BigDecimal

data class RateCurrencyUIContent(
    val currencySymbol: String,
    val currencySymbolDetail: String,
    val price: BigDecimal
)