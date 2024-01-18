package com.example.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.rate.presentation.ListActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashScreenActivity : ComponentActivity() {

    private val vm: SplashScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.uiState.collect {
                    if (it is SplashScreenViewModel.UIState.Success) {
                        startActivity(Intent(
                            this@SplashScreenActivity,
                            ListActivity::class.java
                        ))
                    }
                }
            }
        }
        vm.getCurrencies()
        setContent {
            SplashScreen()
        }
    }
}

