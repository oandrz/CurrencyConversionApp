package com.example.rate

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState: MutableStateFlow<ListUIState> = MutableStateFlow(ListUIState.Loading)
    val uiState: StateFlow<ListUIState> = _uiState.asStateFlow()

    sealed class ListUIState {
        class Success : ListUIState()
        object Loading : ListUIState()
        object Failed : ListUIState()
    }

    sealed class ListUIAction {

        data class OnAmountChanged(val amount: String) : ListUIAction()
        object onBaseCurrencyChanged : ListUIAction()
    }
}
