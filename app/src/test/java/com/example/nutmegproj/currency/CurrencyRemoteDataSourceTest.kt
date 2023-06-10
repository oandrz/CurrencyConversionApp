package com.example.nutmegproj.currency

import com.example.nutmegproj.util.MainDispatcherRule
import com.example.testproject.data.network.currency.CurrencyRemoteDataSource
import com.example.testproject.data.network.currency.CurrencyService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyRemoteDataSourceTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var service: CurrencyService

    private lateinit var objectUnderTest: CurrencyRemoteDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        objectUnderTest = CurrencyRemoteDataSource(service)
    }

    @Test
    fun `given response when get currencies then should return success response`() = runTest {
        val expectation = emptyMap<String, String>()
        coEvery { service.getCurrencies() } returns expectation

        val result = objectUnderTest.getCurrencies()

        assert(expectation == result)
        coVerify { service.getCurrencies() }
    }

    @Test
    fun `given error response when get currencies then should return exception`() = runTest {
        val exception = NullPointerException()
        coEvery { service.getCurrencies() } throws exception

        var isError = false
        try {
            objectUnderTest.getCurrencies()
        } catch (e: NullPointerException) {
            isError = true
        }

        assert(isError)
        coVerify { service.getCurrencies() }
    }
}