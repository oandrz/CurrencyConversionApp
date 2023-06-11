package com.example.nutmegproj

import app.cash.turbine.test
import com.example.nutmegproj.util.MainDispatcherRule
import com.example.testproject.domain.GetCurrenciesUseCase
import com.example.testproject.presentation.splash.SplashScreenViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okio.IOException
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SplashScreenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var getCurrenciesUseCase: GetCurrenciesUseCase

    private lateinit var objectUnderTest: SplashScreenViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        objectUnderTest = SplashScreenViewModel(getCurrenciesUseCase)
    }

    @Test
    fun `given success response when get currencies then should return success state`() = runTest {
        val mockCurrenciesResult = mapOf(
            "JPY" to "Japanese Yen"
        )

        coEvery { getCurrenciesUseCase() } returns flowOf(Result.success(mockCurrenciesResult))

        objectUnderTest.getCurrencies()

        objectUnderTest.uiState.test {
            assertEquals(
                expected = SplashScreenViewModel.UIState.Success,
                actual = awaitItem()
            )
            expectNoEvents()
        }

        coVerify { getCurrenciesUseCase() }
    }

    @Test
    fun `given error response when get currencies then should return success state`() = runTest {
        val testException = IOException("Test message")
        coEvery { getCurrenciesUseCase() } returns flowOf(Result.failure(testException))

        objectUnderTest.getCurrencies()
        objectUnderTest.uiState.test {
            assertEquals(
                expected = SplashScreenViewModel.UIState.Success,
                actual = awaitItem()
            )
            expectNoEvents()
        }

        coVerify { getCurrenciesUseCase() }
    }
}
