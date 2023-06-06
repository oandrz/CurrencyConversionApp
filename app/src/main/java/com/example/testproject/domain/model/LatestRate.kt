package com.example.testproject.domain.model

data class LatestRate(
    val baseRate: CurrencyRate,
    val rates: List<CurrencyRate>
)