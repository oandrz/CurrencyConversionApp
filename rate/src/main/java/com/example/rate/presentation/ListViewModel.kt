package com.example.rate.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rate.data.model.RateResponse
import com.example.rate.domain.GetDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val useCase: GetDataUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<ListUIState> = MutableStateFlow(ListUIState.Loading)
    val uiState: StateFlow<ListUIState> = _uiState.asStateFlow()

    fun init() {
        viewModelScope.launch {
            val response = useCase()
            _uiState.emit(ListUIState.Success(response.prices))
        }
    }

    sealed class ListUIState {
        class Success(val flightRates: List<RateResponse>) : ListUIState()
        object Loading : ListUIState()
        object Failed : ListUIState()
    }

    sealed class ListUIAction {


    }
}
