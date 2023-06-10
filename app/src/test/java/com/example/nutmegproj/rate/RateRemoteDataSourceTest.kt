package com.example.nutmegproj.rate

import com.example.nutmegproj.util.MainDispatcherRule
import com.example.testproject.data.network.rate.RateRemoteDataSource
import com.example.testproject.data.network.rate.RateService
import com.example.testproject.data.network.rate.response.LatestRateResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RateRemoteDataSourceTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var service: RateService

    private lateinit var objectUnderTest: RateRemoteDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        objectUnderTest = RateRemoteDataSource(service)
    }

    @Test
    fun `given response when get currencies then should return success response`() = runTest {
        val expectation = LatestRateResponse(
            timestamp = 0,
            baseCurrency = "USD",
            rates = emptyMap()
        )
        coEvery { service.getLatestRate() } returns expectation

        val result = objectUnderTest.getLatestRate()

        assert(expectation == result)
    }

    @Test
    fun `given error response when get currencies then should return success response`() = runTest {
        val exception = NullPointerException()
        coEvery { service.getLatestRate() } throws exception

        var isError = false
        try {
            objectUnderTest.getLatestRate()
        } catch (e: NullPointerException) {
            isError = true
        }

        assert(isError)
    }
}