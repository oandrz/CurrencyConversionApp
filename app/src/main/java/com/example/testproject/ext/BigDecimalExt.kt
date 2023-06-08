package com.example.testproject.ext

import com.example.testproject.domain.model.CurrencyRate
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

fun BigDecimal.toDecimal(): String = DecimalFormat("#.##").let {
    it.roundingMode = RoundingMode.UP
    it.format(this)
}

fun String.convertTo(amount: BigDecimal, toCurrency: String, currencySource: List<CurrencyRate>) : BigDecimal {
    val rateDict = currencySource.associate { it.currency to it.rate }
    val valueFromCurrencyInDollar = this.convertAnyCurrencyToDollar(amount, rateDict)

    return convertDollarToAnyCurrency(valueFromCurrencyInDollar, toCurrency, rateDict)
}

private fun String.convertAnyCurrencyToDollar(amount: BigDecimal, rateDict: Map<String, Double>): BigDecimal {
    val currencyRate = rateDict[this] ?: 0.0
    val scale = 50
    return amount.divide(currencyRate.toBigDecimal(), scale, RoundingMode.HALF_UP)
}

private fun convertDollarToAnyCurrency(dollarValue: BigDecimal, toCurrency: String, rateDict: Map<String, Double>): BigDecimal {
    val currencyRate = rateDict[toCurrency] ?: 0.0
    return dollarValue.multiply(currencyRate.toBigDecimal())
}