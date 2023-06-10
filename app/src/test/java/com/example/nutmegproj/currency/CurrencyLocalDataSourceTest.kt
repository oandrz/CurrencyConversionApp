package com.example.nutmegproj.currency

import com.example.nutmegproj.util.MainDispatcherRule
import com.example.testproject.data.local.currency.CacheCurrency
import com.example.testproject.data.local.currency.CurrencyLocalDataSource
import com.example.testproject.data.local.db.CurrencyDao
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
class CurrencyLocalDataSourceTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var dao: CurrencyDao

    private lateinit var objectUnderTest: CurrencyLocalDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        objectUnderTest = CurrencyLocalDataSource(dao)
    }

    @Test
    fun `given cache data when get currencies from cache then should return success response`() = runTest {
        val expectation = listOf(
            CacheCurrency(
                currencyDictionary = "{}",
                timestamp = 0L
            )
        )
        coEvery { dao.getCurrencyDict() } returns expectation

        val result = objectUnderTest.getCurrencies()

        assert(expectation == result)
        coVerify { dao.getCurrencyDict() }
    }

    @Test
    fun `given error response when get currencies from cache then should return exception`() = runTest {
        val exception = NullPointerException()
        coEvery { dao.getCurrencyDict() } throws exception

        var isError = false
        try {
            objectUnderTest.getCurrencies()
        } catch (e: NullPointerException) {
            isError = true
        }

        assert(isError)
        coVerify { dao.getCurrencyDict() }
    }

    @Test
    fun `given a cache when save cache then should call dao`() = runTest {
        val param = CacheCurrency(
            currencyDictionary = "{}",
            timestamp = 0L
        )

        coEvery { dao.insert(param) } returns Unit

        objectUnderTest.saveCurrencies(param)

        coVerify { dao.insert(param) }
    }
}