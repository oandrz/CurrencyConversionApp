package com.example.nutmegproj.rate

import com.example.nutmegproj.util.MainDispatcherRule
import com.example.testproject.data.local.rate.CachedRate
import com.example.testproject.data.local.rate.RateDao
import com.example.testproject.data.local.rate.RateLocalDataSource
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
class RateLocalDataSourceTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var dao: RateDao

    private lateinit var objectUnderTest: RateLocalDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        objectUnderTest = RateLocalDataSource(dao)
    }

    @Test
    fun `given cache data when get currencies from cache then should return success response`() = runTest {
        val expectation = listOf(
            CachedRate(
                rateData = "USD",
                createdAtTimeStamp = 0L
            )
        )
        coEvery { dao.getCachedRate() } returns expectation

        val result = objectUnderTest.getCachedRate()

        assert(expectation == result)
        coVerify { dao.getCachedRate() }
    }

    @Test
    fun `given error response when get currencies from cache then should return exception`() = runTest {
        val exception = NullPointerException()
        coEvery { dao.getCachedRate() } throws exception

        var isError = false
        try {
            objectUnderTest.getCachedRate()
        } catch (e: NullPointerException) {
            isError = true
        }

        assert(isError)
        coVerify { dao.getCachedRate() }
    }

    @Test
    fun `given cache data when insert currencies from cache then should call dao`() = runTest {
        val param = CachedRate(
            rateData = "USD",
            createdAtTimeStamp = 0L
        )
        coEvery { dao.insert(param) } returns Unit

        objectUnderTest.saveRate(param)

        coVerify { dao.insert(param) }
    }
}