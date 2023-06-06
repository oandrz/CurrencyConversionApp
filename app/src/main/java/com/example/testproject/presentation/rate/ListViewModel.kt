package com.example.testproject.presentation.rate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testproject.domain.GetCurrenciesUseCase
import com.example.testproject.domain.GetLatestRateUseCase
import com.example.testproject.domain.model.CurrencyRate
import com.example.testproject.domain.model.LatestRate
import com.example.testproject.presentation.rate.model.BaseRateCurrencyUIContent
import com.example.testproject.presentation.rate.model.RateCurrencyUIContent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
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

    private val _baseCurrencyAmount: MutableStateFlow<Double> = MutableStateFlow(1.0)
    private val baseCurrencyAmount: Flow<Double> = _baseCurrencyAmount

    private val _currencySymbolDictionary: MutableStateFlow<Map<String, String>> = MutableStateFlow(emptyMap())
    private val currencySymbolDictionary: Flow<Map<String, String>> = _currencySymbolDictionary

    private val _rate: MutableSharedFlow<LatestRate> = MutableSharedFlow()
    private val rate: Flow<LatestRate> = _rate

    init {
        viewModelScope.launch {
            _baseCurrencyAmount.onStart { emit(1.0) }
            combine(baseCurrencyAmount, currencySymbolDictionary, rate) { amount, currencyDict, latestRate ->
                if (cachedCurrentRate == null) {
                    cachedCurrentRate = latestRate
                }
                val rateList = cachedCurrentRate?.rates
                    ?.filter { it.currency != cachedCurrentRate?.baseRate?.currency }
                    ?.map {
                        RateCurrencyUIContent(
                            currencySymbol = it.currency,
                            currencySymbolDetail = currencyDict[it.currency].orEmpty(),
                            price = it.rate * amount
                        )
                    }
                val baseRate = BaseRateCurrencyUIContent(
                    currency = RateCurrencyUIContent(
                        currencySymbol = cachedCurrentRate?.baseRate?.currency.orEmpty(),
                        currencySymbolDetail = currencyDict[cachedCurrentRate?.baseRate?.currency].orEmpty(),
                        price = 0.0
                    ),
                    amount = amount
                )

                baseRate to rateList
            }.collect {
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
                .collect { result ->
                    result.second
                        .onSuccess {
                            _currencySymbolDictionary.emit(result.first.getOrDefault(emptyMap()))
                            _rate.emit(it)
                        }
                        .onFailure {
                            Timber.e(it)
                            _uiState.emit(ListUIState.Failed)
                        }
                }
        }
    }

    fun onAmountChange(text: String) {
        viewModelScope.launch {
            _baseCurrencyAmount.emit(text.toDouble())
        }
    }

    fun onCurrencyClick(rate: CurrencyRate) {

    }

    sealed class ListUIState {
        data class Success(
            val baseRate: BaseRateCurrencyUIContent,
            val rates: List<RateCurrencyUIContent>
        ) : ListUIState()
        object Loading : ListUIState()
        object Failed : ListUIState()
    }
}
