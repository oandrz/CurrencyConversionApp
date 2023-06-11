package com.example.nutmegproj

import app.cash.turbine.test
import com.example.nutmegproj.util.MainDispatcherRule
import com.example.testproject.domain.GetCurrenciesUseCase
import com.example.testproject.domain.GetLatestRateUseCase
import com.example.testproject.domain.model.CurrencyRate
import com.example.testproject.domain.model.LatestRate
import com.example.testproject.presentation.rate.ListViewModel
import com.example.testproject.presentation.rate.model.BaseRateCurrencyUIContent
import com.example.testproject.presentation.rate.model.RateCurrencyUIContent
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
class ListScreenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var getLatestRateUseCase: GetLatestRateUseCase

    @MockK
    private lateinit var getCurrenciesUseCase: GetCurrenciesUseCase

    private lateinit var objectUnderTest: ListViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        objectUnderTest = ListViewModel(
            getLatestRateUseCase,
            getCurrenciesUseCase
        )
    }

    @Test
    fun `given success response with base currencies is a sole item response when get latest rate then should return success state`() =
        runTest {
            val mockCurrenciesResult = mapOf(
                "JPY" to "Japanese Yen"
            )
            val mockLatestRateResult = LatestRate(
                baseRate = CurrencyRate(
                    0.0,
                    "JPY"
                ),
                rates = listOf(
                    CurrencyRate(
                        0.0,
                        "JPY"
                    )
                )
            )
            val expectedUIContent = ListViewModel.ListUIState.Success(
                baseRate = BaseRateCurrencyUIContent(
                    currency = RateCurrencyUIContent(
                        currencySymbol = "JPY",
                        currencySymbolDetail = "Japanese Yen",
                        price = 0.0.toBigDecimal()
                    ),
                    amount = 1.0.toBigDecimal()
                ),
                rates = emptyList()
            )

            coEvery { getCurrenciesUseCase() } returns flowOf(Result.success(mockCurrenciesResult))
            coEvery { getLatestRateUseCase() } returns flowOf(Result.success(mockLatestRateResult))

            objectUnderTest.getLatestRates()

            objectUnderTest.uiState.test {
                val actual = awaitItem()
                assertEquals(
                    expected = ListViewModel.ListUIState.Loading,
                    actual = actual
                )
                assertEquals(
                    expected = expectedUIContent,
                    actual = awaitItem()
                )
                expectNoEvents()
            }

            coVerify { getCurrenciesUseCase() }
            coVerify { getLatestRateUseCase() }
        }

    @Test
    fun `given success response when get latest rate then should return success state`() =
        runTest {
            val mockCurrenciesResult = mapOf(
                "JPY" to "Japanese Yen",
                "USD" to "United States Dollar"
            )
            val mockLatestRateResult = LatestRate(
                baseRate = CurrencyRate(
                    1.0,
                    "JPY"
                ),
                rates = listOf(
                    CurrencyRate(
                        1.0,
                        "JPY"
                    ),
                    CurrencyRate(
                        1.0,
                        "USD"
                    )
                )
            )
            val expectedUIContent = ListViewModel.ListUIState.Success(
                baseRate = BaseRateCurrencyUIContent(
                    currency = RateCurrencyUIContent(
                        currencySymbol = "JPY",
                        currencySymbolDetail = "Japanese Yen",
                        price = 1.0.toBigDecimal()
                    ),
                    amount = 1.0.toBigDecimal()
                ),
                rates = listOf(
                    RateCurrencyUIContent(
                        currencySymbol = "USD",
                        currencySymbolDetail = "United States Dollar",
                        price = 1.0.toBigDecimal().setScale(51)
                    )
                )
            )

            coEvery { getCurrenciesUseCase() } returns flowOf(Result.success(mockCurrenciesResult))
            coEvery { getLatestRateUseCase() } returns flowOf(Result.success(mockLatestRateResult))

            objectUnderTest.getLatestRates()

            objectUnderTest.uiState.test {
                val actual = awaitItem()
                assertEquals(
                    expected = ListViewModel.ListUIState.Loading,
                    actual = actual
                )
                assertEquals(
                    expected = expectedUIContent,
                    actual = awaitItem()
                )
                expectNoEvents()
            }

            coVerify { getCurrenciesUseCase() }
            coVerify { getLatestRateUseCase() }
        }

    @Test
    fun `given error response when get currencies from cache then should return failure state`() = runTest {
        val mockCurrenciesResult = mapOf(
            "JPY" to "Japanese Yen",
            "USD" to "United States Dollar"
        )
        val testException = IOException("Test message")
        coEvery { getCurrenciesUseCase() } returns flowOf(Result.success(mockCurrenciesResult))
        coEvery { getLatestRateUseCase() } returns flowOf(Result.failure(testException))

        objectUnderTest.getLatestRates()
        objectUnderTest.uiState.test {
            assertEquals(
                expected = ListViewModel.ListUIState.Loading,
                actual = awaitItem()
            )
            assertEquals(
                expected = ListViewModel.ListUIState.Failed,
                actual = awaitItem()
            )
            expectNoEvents()
        }

        coVerify { getCurrenciesUseCase() }
        coVerify { getLatestRateUseCase() }
    }

    @Test
    fun `given amount changed when action invoked amount changed then should update amount state`() = runTest {
        val mockCurrenciesResult = mapOf(
            "JPY" to "Japanese Yen"
        )
        val mockLatestRateResult = LatestRate(
            baseRate = CurrencyRate(
                0.0,
                "JPY"
            ),
            rates = listOf(
                CurrencyRate(
                    0.0,
                    "JPY"
                )
            )
        )
        val expectedUIContent = ListViewModel.ListUIState.Success(
            baseRate = BaseRateCurrencyUIContent(
                currency = RateCurrencyUIContent(
                    currencySymbol = "JPY",
                    currencySymbolDetail = "Japanese Yen",
                    price = 0.0.toBigDecimal()
                ),
                amount = 1.0.toBigDecimal()
            ),
            rates = emptyList()
        )
        val expectedUIContentAfterAmountChanged = ListViewModel.ListUIState.Success(
            baseRate = BaseRateCurrencyUIContent(
                currency = RateCurrencyUIContent(
                    currencySymbol = "JPY",
                    currencySymbolDetail = "Japanese Yen",
                    price = 0.0.toBigDecimal()
                ),
                amount = 2.5.toBigDecimal()
            ),
            rates = emptyList()
        )
        val action = ListViewModel.ListUIAction.OnAmountChanged("2.5")

        coEvery { getCurrenciesUseCase() } returns flowOf(Result.success(mockCurrenciesResult))
        coEvery { getLatestRateUseCase() } returns flowOf(Result.success(mockLatestRateResult))

        objectUnderTest.getLatestRates()

        objectUnderTest.uiState.test {
            assertEquals(
                expected = ListViewModel.ListUIState.Loading,
                actual = awaitItem()
            )
            assertEquals(
                expected = expectedUIContent,
                actual = awaitItem()
            )
            expectNoEvents()
        }
        coVerify { getCurrenciesUseCase() }
        coVerify { getLatestRateUseCase() }

        objectUnderTest.onActionInvoked(action)
        objectUnderTest.uiState.test {
            assertEquals(
                expected = expectedUIContentAfterAmountChanged,
                actual = awaitItem()
            )
            expectNoEvents()
        }
        coVerify { getCurrenciesUseCase() }
        coVerify { getLatestRateUseCase() }
    }

    @Test
    fun `given base currency changed when action invoked amount changed then should update base currency`() = runTest {
        val mockCurrenciesResult = mapOf(
            "JPY" to "Japanese Yen",
            "USD" to "United States Dollar"
        )
        val mockLatestRateResult = LatestRate(
            baseRate = CurrencyRate(
                1.0,
                "JPY"
            ),
            rates = listOf(
                CurrencyRate(
                    1.0,
                    "JPY"
                ),
                CurrencyRate(
                    1.0,
                    "USD"
                )
            )
        )
        val expectedUIContent = ListViewModel.ListUIState.Success(
            baseRate = BaseRateCurrencyUIContent(
                currency = RateCurrencyUIContent(
                    currencySymbol = "JPY",
                    currencySymbolDetail = "Japanese Yen",
                    price = 1.0.toBigDecimal()
                ),
                amount = 1.0.toBigDecimal()
            ),
            rates = listOf(
                RateCurrencyUIContent(
                    currencySymbol = "USD",
                    currencySymbolDetail = "United States Dollar",
                    price = 1.0.toBigDecimal().setScale(51)
                )
            )
        )

        coEvery { getCurrenciesUseCase() } returns flowOf(Result.success(mockCurrenciesResult))
        coEvery { getLatestRateUseCase() } returns flowOf(Result.success(mockLatestRateResult))

        objectUnderTest.getLatestRates()

        objectUnderTest.uiState.test {
            val actual = awaitItem()
            assertEquals(
                expected = ListViewModel.ListUIState.Loading,
                actual = actual
            )
            assertEquals(
                expected = expectedUIContent,
                actual = awaitItem()
            )
            expectNoEvents()
        }

        coVerify { getCurrenciesUseCase() }
        coVerify { getLatestRateUseCase() }

        val action = ListViewModel.ListUIAction.onBaseCurrencyChanged(
            RateCurrencyUIContent(
                currencySymbolDetail = "United States Dollar",
                currencySymbol = "USD",
                price = 1.0.toBigDecimal()
            )
        )
        val expectedUIContentAfterBaseChanged = ListViewModel.ListUIState.Success(
            baseRate = BaseRateCurrencyUIContent(
                currency = RateCurrencyUIContent(
                    currencySymbol = "USD",
                    currencySymbolDetail = "United States Dollar",
                    price = 1.0.toBigDecimal()
                ),
                amount = 1.0.toBigDecimal()
            ),
            rates = listOf(
                RateCurrencyUIContent(
                    currencySymbol = "JPY",
                    currencySymbolDetail = "Japanese Yen",
                    price = 1.0.toBigDecimal().setScale(51)
                )
            )
        )
        objectUnderTest.onActionInvoked(action)
        objectUnderTest.uiState.test {
            assertEquals(
                expected = expectedUIContentAfterBaseChanged,
                actual = awaitItem()
            )
            expectNoEvents()
        }

        coVerify { getCurrenciesUseCase() }
        coVerify { getLatestRateUseCase() }
    }
}
