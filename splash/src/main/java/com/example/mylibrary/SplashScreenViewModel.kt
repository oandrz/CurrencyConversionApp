package com.example.mylibrary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState: MutableStateFlow<UIState> = MutableStateFlow(UIState.Loading)
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    fun getCurrencies() {
        viewModelScope.launch {
            _uiState.emit(UIState.Loading)
        }
    }

    sealed class UIState {
        object Loading : UIState()
        object Success : UIState()
    }
}