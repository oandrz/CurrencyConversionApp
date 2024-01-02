package com.example.rate.model

import java.math.BigDecimal

data class BaseRateCurrencyUIContent(
    val currency: RateCurrencyUIContent,
    val amount: BigDecimal
)