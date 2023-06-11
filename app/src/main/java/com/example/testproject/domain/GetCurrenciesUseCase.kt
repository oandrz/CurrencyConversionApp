package com.example.testproject.domain

import com.example.testproject.data.CurrencyRepository
import com.example.testproject.ext.resultOf
import com.example.testproject.ext.toResult
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

class GetCurrenciesUseCase @Inject constructor(
    private val repository: CurrencyRepository
) {

    operator fun invoke(): Flow<Result<Map<String, String>>> =
        repository.getCurrencies().toResult()
            .transform { response ->
                val result = response.getOrDefault(emptyList())
                emit(
                    resultOf {
                        result.associate { it.currencySymbol to it.currency }
                    }
                )
            }

}