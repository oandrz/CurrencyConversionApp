package com.example.testproject.presentation.rate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testproject.domain.GetCurrenciesUseCase
import com.example.testproject.domain.GetLatestRateUseCase
import com.example.testproject.domain.model.LatestRate
import com.example.testproject.ext.convertTo
import com.example.testproject.presentation.rate.model.BaseRateCurrencyUIContent
import com.example.testproject.presentation.rate.model.RateCurrencyUIContent
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigDecimal
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class ListViewModel @Inject constructor(
    private val getLatestRateUseCase: GetLatestRateUseCase,
    private val getCurrenciesUseCase: GetCurrenciesUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<ListUIState> = MutableStateFlow(ListUIState.Loading)
    val uiState: StateFlow<ListUIState> = _uiState.asStateFlow()

    private var cachedCurrentRate: LatestRate? = null

    private val _baseCurrencyAmount: MutableStateFlow<BigDecimal> = MutableStateFlow(1.0.toBigDecimal())
    private val baseCurrencyAmount: Flow<BigDecimal> = _baseCurrencyAmount

    private val _baseCurrency: MutableSharedFlow<RateCurrencyUIContent> = MutableSharedFlow()
    private val baseCurrency: Flow<RateCurrencyUIContent> =_baseCurrency

    private val _currencySymbolDictionary: MutableStateFlow<Map<String, String>> = MutableStateFlow(emptyMap())
    private val currencySymbolDictionary: Flow<Map<String, String>> = _currencySymbolDictionary

    private val _rate: MutableSharedFlow<LatestRate> = MutableSharedFlow()
    private val rate: Flow<LatestRate> = _rate

    init {
        viewModelScope.launch {
            combine(
                baseCurrency,
                baseCurrencyAmount,
                currencySymbolDictionary,
                rate
            ) { baseCurrency, amount, currencyDict, latestRate ->
                if (cachedCurrentRate == null) {
                    cachedCurrentRate = latestRate
                }
                val rateList = cachedCurrentRate?.rates
                    ?.filter { it.currency != baseCurrency.currencySymbol }
                    ?.map {
                        RateCurrencyUIContent(
                            currencySymbol = it.currency,
                            currencySymbolDetail = currencyDict[it.currency].orEmpty(),
                            price = baseCurrency.currencySymbol.convertTo(
                                amount,
                                it.currency,
                                currencySource = cachedCurrentRate?.rates ?: emptyList()
                            )
                        )
                    }
                val baseRate = BaseRateCurrencyUIContent(
                    currency = baseCurrency,
                    amount = amount
                )

                baseRate to rateList
            }.collectLatest {
                _uiState.emit(ListUIState.Success(it.first, it.second ?: emptyList()))
            }
        }
    }

    fun getLatestRates() {
        viewModelScope.launch {
            _uiState.emit(ListUIState.Loading)

            getCurrenciesUseCase().combine(getLatestRateUseCase()) { currencyResult, latestRateResult ->
                currencyResult to latestRateResult
            }
                .flowOn(Dispatchers.IO)
                .collect { (dictionary, response) ->
                    response
                        .onSuccess {
                            val symbolDictionary = dictionary.getOrDefault(emptyMap())
                            val baseRate = RateCurrencyUIContent(
                                currencySymbolDetail = symbolDictionary[it.baseRate.currency].orEmpty(),
                                currencySymbol = it.baseRate.currency,
                                price = it.baseRate.rate.toBigDecimal()
                            )
                            _baseCurrency.emit(baseRate)
                            _currencySymbolDictionary.emit(symbolDictionary)
                            _rate.emit(it)
                        }
                        .onFailure {
                            Timber.e(it)
                            _uiState.emit(ListUIState.Failed)
                        }
                }
        }
    }

    private fun onAmountChange(text: String) {
        viewModelScope.launch {
            _baseCurrencyAmount.emit(text.toBigDecimal())
        }
    }

    private fun onCurrencyClick(rate: RateCurrencyUIContent) {
        viewModelScope.launch {
            _baseCurrency.emit(rate)
        }
    }

    fun onActionInvoked(action: ListUIAction) {
        when (action) {
            is ListUIAction.OnAmountChanged -> onAmountChange(action.amount)
            is ListUIAction.onBaseCurrencyChanged -> onCurrencyClick(action.rate)
        }
    }

    sealed class ListUIState {
        data class Success(
            val baseRate: BaseRateCurrencyUIContent,
            val rates: List<RateCurrencyUIContent>
        ) : ListUIState()
        object Loading : ListUIState()
        object Failed : ListUIState()
    }

    sealed class ListUIAction {

        data class OnAmountChanged(val amount: String) : ListUIAction()
        data class onBaseCurrencyChanged(val rate: RateCurrencyUIContent) : ListUIAction()
    }
}
