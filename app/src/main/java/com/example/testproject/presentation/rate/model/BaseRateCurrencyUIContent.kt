package com.example.testproject.presentation.rate.model

import java.math.BigDecimal

data class BaseRateCurrencyUIContent(
    val currency: RateCurrencyUIContent,
    val amount: BigDecimal
)