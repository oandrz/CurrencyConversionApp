package com.example.nutmegproj.currency

import app.cash.turbine.test
import com.example.nutmegproj.util.MainDispatcherRule
import com.example.testproject.data.CurrencyRepository
import com.example.testproject.domain.GetCurrenciesUseCase
import com.example.testproject.domain.model.CurrencyDetail
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
import org.junit.jupiter.api.assertThrows

@OptIn(ExperimentalCoroutinesApi::class)
class GetCurrencyUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var repository: CurrencyRepository

    private lateinit var objectUnderTest: GetCurrenciesUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        objectUnderTest = GetCurrenciesUseCase(repository)
    }

    @Test
    fun `given success response when get latest rate then should return success result`() = runTest {
        val mockLatestRate = listOf(
            CurrencyDetail(
                currency = "Japanese Yen",
                currencySymbol = "JPY"
            )
        )
        val expectation = mapOf(
            "JPY" to "Japanese Yen"
        )
        coEvery { repository.getCurrencies() } returns flowOf(mockLatestRate)

        objectUnderTest().test {
            val result = awaitItem()
            assertEquals(
                expected = Result.success(expectation),
                actual = result
            )
            awaitComplete()
        }

        coVerify { repository.getCurrencies() }
    }

    @Test
    fun `given error response when get currencies from cache then should return exception`() = runTest {
        val mockLatestRate = listOf(
            CurrencyDetail(
                currency = "Japanese Yen",
                currencySymbol = "JPY"
            )
        )
        val expectation = mapOf(
            "JPY" to "Japanese Yen"
        )
        val testException = IOException("Test message")
        coEvery { repository.getCurrencies() } throws testException andThen flowOf(mockLatestRate)

        assertThrows<IOException> {
            objectUnderTest().test {
                val response = awaitItem()
                assertEquals(
                    expected = Result.failure(testException),
                    actual = response
                )

                val itemsResult = awaitItem()
                assertEquals(
                    expected = Result.success(expectation),
                    actual = itemsResult
                )
            }
        }

        coVerify { repository.getCurrencies() }
    }
}