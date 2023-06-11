package com.example.nutmegproj.rate

import app.cash.turbine.test
import com.example.nutmegproj.util.MainDispatcherRule
import com.example.testproject.data.RateRepository
import com.example.testproject.domain.GetLatestRateUseCase
import com.example.testproject.domain.model.CurrencyRate
import com.example.testproject.domain.model.LatestRate
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
class GetLatestRateUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var rateRepository: RateRepository

    private lateinit var objectUnderTest: GetLatestRateUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        objectUnderTest = GetLatestRateUseCase(rateRepository)
    }

    @Test
    fun `given success response when get latest rate then should return success result`() = runTest {
        val mockLatestRate = LatestRate(
            baseRate = CurrencyRate(rate = 1.0, currency = "USD"),
            rates = emptyList()
        )
        val expectation = Result.success(mockLatestRate)
        coEvery { rateRepository.getLatestRates() } returns flowOf(mockLatestRate)

        objectUnderTest().test {
            assert(expectation == expectMostRecentItem())
        }

        coVerify { rateRepository.getLatestRates() }
    }

    @Test
    fun `given error response when get currencies from cache then should return exception`() = runTest {
        val mockLatestRate = LatestRate(
            baseRate = CurrencyRate(rate = 1.0, currency = "USD"),
            rates = emptyList()
        )
        val testException = IOException("Test message")
        coEvery { rateRepository.getLatestRates() } throws testException andThen flowOf(mockLatestRate)

        assertThrows<IOException> {
            objectUnderTest().test {
                val response = awaitItem()
                assertEquals(
                    expected = Result.failure(testException),
                    actual = response
                )

                val itemsResult = awaitItem()
                assertEquals(
                    expected = Result.success(mockLatestRate),
                    actual = itemsResult
                )
            }
        }

        coVerify { rateRepository.getLatestRates() }
    }
}