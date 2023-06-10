package com.example.nutmegproj.currency

import app.cash.turbine.test
import com.example.nutmegproj.util.MainDispatcherRule
import com.example.testproject.data.CurrencyRepository
import com.example.testproject.data.CurrencyRepositoryImpl
import com.example.testproject.data.local.currency.CacheCurrency
import com.example.testproject.data.local.currency.CurrencyLocalDataSource
import com.example.testproject.data.network.currency.CurrencyRemoteDataSource
import com.example.testproject.domain.model.CurrencyDetail
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
class CurrencyRepositoryTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var localDataSource: CurrencyLocalDataSource

    @MockK
    private lateinit var remoteDataSource: CurrencyRemoteDataSource

    private lateinit var objectUnderTest: CurrencyRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        objectUnderTest = CurrencyRepositoryImpl(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource
        )
    }

    @Test
    fun `on first init when get currencies will get from remote data source`() = runTest {
        val mockResponse = mapOf(
            "JPY" to "Japanese Yen",
            "USD" to "United States Dollar",
            "IDR" to "Indonesia Rupiah",
        )
        coEvery { localDataSource.getCurrencies() } returns emptyList()
        coEvery { localDataSource.saveCurrencies(any()) } returns Unit
        coEvery { remoteDataSource.getCurrencies() } returns mockResponse

        val expectation = listOf(
            CurrencyDetail("JPY", "Japanese Yen"),
            CurrencyDetail("USD", "United States Dollar"),
            CurrencyDetail("IDR", "Indonesia Rupiah"),
        )

        objectUnderTest.getCurrencies().test {
            assert(expectation == expectMostRecentItem())
        }

        coVerify { localDataSource.getCurrencies() }
        coVerify { localDataSource.saveCurrencies(any()) }
        coVerify { remoteDataSource.getCurrencies() }
    }

    @Test
    fun `on second init when get currency will get from cache`() = runTest {
        val cached = listOf(
            CacheCurrency(
                id = 0,
                "{ \"JPY\" : \"Japanese Yen\" }",
                timestamp = System.currentTimeMillis()
            )
        )
        coEvery { localDataSource.getCurrencies() } returns cached

        val expectation = listOf(
            CurrencyDetail("JPY", "Japanese Yen"),
        )

        objectUnderTest.getCurrencies().test {
            assert(expectation == expectMostRecentItem())
        }

        coVerify { localDataSource.getCurrencies() }
    }

    @Test
    fun `given expired cache when get currency will get from remote`() = runTest {
        val cached = listOf(
            CacheCurrency(
                id = 0,
                "{ \"JPY\" : \"Japanese Yen\" }",
                timestamp = System.currentTimeMillis() - THIRTY_MINUTE_MILLIS
            )
        )
        val mockResponse = mapOf(
            "JPY" to "Japanese Yen",
            "USD" to "United States Dollar",
            "IDR" to "Indonesia Rupiah",
        )
        coEvery { localDataSource.saveCurrencies(any()) } returns Unit
        coEvery { remoteDataSource.getCurrencies() } returns mockResponse
        coEvery { localDataSource.getCurrencies() } returns cached

        val expectation = listOf(
            CurrencyDetail("JPY", "Japanese Yen"),
            CurrencyDetail("USD", "United States Dollar"),
            CurrencyDetail("IDR", "Indonesia Rupiah"),
        )

        objectUnderTest.getCurrencies().test {
            assert(expectation == expectMostRecentItem())
        }

        coVerify { localDataSource.getCurrencies() }
        coVerify { localDataSource.saveCurrencies(any()) }
        coVerify { remoteDataSource.getCurrencies() }
    }

    companion object {
        const val THIRTY_MINUTE_MILLIS = 1_800_000
    }
}