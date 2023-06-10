package com.example.nutmegproj.rate

import app.cash.turbine.test
import com.example.nutmegproj.util.MainDispatcherRule
import com.example.testproject.data.RateRepository
import com.example.testproject.data.RateRepositoryImpl
import com.example.testproject.data.local.rate.CachedRate
import com.example.testproject.data.local.rate.RateLocalDataSource
import com.example.testproject.data.network.rate.LatestRateResponseTransformer
import com.example.testproject.data.network.rate.RateRemoteDataSource
import com.example.testproject.data.network.rate.response.LatestRateResponse
import com.example.testproject.domain.model.CurrencyRate
import com.example.testproject.domain.model.LatestRate
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RateRepositoryTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var localDataSource: RateLocalDataSource

    @MockK
    private lateinit var remoteDataSource: RateRemoteDataSource

    @MockK
    private lateinit var transformer: LatestRateResponseTransformer

    private lateinit var objectUnderTest: RateRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        objectUnderTest = RateRepositoryImpl(
            rateRemoteDataSource = remoteDataSource,
            rateLocalDataSource = localDataSource,
            rateResponseTransformer = transformer
        )
    }

    @Test
    fun `on first init when get latest raet will get from remote data source`() = runTest {
        val mockRates = mapOf(
            "JPY" to 1.0,
            "USD" to 2.0,
            "IDR" to 3.0,
        )
        val mockResponse = LatestRateResponse(
            timestamp = System.currentTimeMillis(),
            baseCurrency = "JPY",
            rates = mockRates
        )
        val expectation = LatestRate(
            baseRate = CurrencyRate(
                rate = 1.0,
                currency = "USD"
            ),
            emptyList()
        )
        coEvery { localDataSource.getCachedRate() } returns emptyList()
        coEvery { localDataSource.saveRate(any()) } returns Unit
        coEvery { remoteDataSource.getLatestRate() } returns mockResponse
        coEvery { transformer(any()) } returns expectation

        objectUnderTest.getLatestRates().test {
            assert(expectation == expectMostRecentItem())
        }

        coVerify { localDataSource.getCachedRate() }
        coVerify { localDataSource.saveRate(any()) }
        coVerify { remoteDataSource.getLatestRate() }
        coVerify { transformer(any()) }
    }

    @Test
    fun `on second init when get currency will get from cache`() = runTest {
        val cached = listOf(
            CachedRate(
                id = 0,
                rateData = """
                    {
                        "timestamp": 1686384004,
                        "base": "USD",
                        "rates": {
                            "AED": 3.673
                        }
                    }
                """.trimIndent(),
                createdAtTimeStamp = System.currentTimeMillis()
            )
        )
        val expectation = LatestRate(
            baseRate = CurrencyRate(
                rate = 1.0,
                currency = "USD"
            ),
            emptyList()
        )
        coEvery { localDataSource.getCachedRate() } returns cached
        coEvery { transformer(any()) } returns expectation

        objectUnderTest.getLatestRates().test {
            assert(expectation == expectMostRecentItem())
        }

        coVerify { localDataSource.getCachedRate() }
    }

    @Test
    fun `given expired cache when get currency will get from remote`() = runTest {
        val cached = listOf(
            CachedRate(
                id = 0,
                rateData = """
                    {
                        "timestamp": 1686384004,
                        "base": "USD",
                        "rates": {
                            "AED": 3.673
                        }
                    }
                """.trimIndent(),
                createdAtTimeStamp = System.currentTimeMillis() - THIRTY_MINUTE_MILLIS
            )
        )
        val mockRates = mapOf(
            "JPY" to 1.0,
            "USD" to 2.0,
            "IDR" to 3.0,
        )
        val mockResponse = LatestRateResponse(
            timestamp = System.currentTimeMillis(),
            baseCurrency = "JPY",
            rates = mockRates
        )
        val expectation = LatestRate(
            baseRate = CurrencyRate(
                rate = 1.0,
                currency = "USD"
            ),
            emptyList()
        )
        coEvery { localDataSource.saveRate(any()) } returns Unit
        coEvery { remoteDataSource.getLatestRate() } returns mockResponse
        coEvery { localDataSource.getCachedRate() } returns cached
        coEvery { transformer(any()) } returns expectation

        objectUnderTest.getLatestRates().test {
            assert(expectation == expectMostRecentItem())
        }

        coVerify { localDataSource.getCachedRate() }
        coVerify { localDataSource.saveRate(any()) }
        coVerify { remoteDataSource.getLatestRate() }
        coVerify { transformer(any()) }
    }

    companion object {
        const val THIRTY_MINUTE_MILLIS = 1_800_000
    }
}