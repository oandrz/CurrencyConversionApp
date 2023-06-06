package com.example.nutmegproj

import com.example.nutmegproj.data.network.CurrencyRemoteDataSource
import com.example.nutmegproj.data.network.CurrencyService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RemoteDataSourceTest {

    @MockK
    private lateinit var service: CurrencyService

    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var objectUnderTest: CurrencyRemoteDataSource

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        objectUnderTest = CurrencyRemoteDataSource(service)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
        unmockkAll()
    }

    @Test
    fun `given a valid response when fetch photos then should return the response`() = runTest {
        val expected = listOf(photoResponse)
        coEvery { service.getPhotos() } returns expected

        objectUnderTest.getPhotos().collect()

        coVerify { service.getPhotos() }
    }

    @Test
    fun `given a exception when fetch photos then should return the exception`() = runTest {
        val exception = NullPointerException()
        coEvery { service.getPhotos() } throws exception

        var isExceptionThrown = false
        try {
            objectUnderTest.getPhotos().collect()
        } catch (e: NullPointerException) {
            isExceptionThrown = true
        }

        assert(isExceptionThrown)
        coVerify { service.getPhotos() }
    }

    @Test
    fun `given a valid response when fetch users then should return the response`() = runTest {
        val expected = listOf(userResponse)
        coEvery { service.getUsers() } returns expected

        objectUnderTest.getUsers().collect()

        coVerify { service.getUsers() }
    }

    @Test
    fun `given a exception when fetch users then should return the exception`() = runTest {
        val exception = NullPointerException()
        coEvery { service.getUsers() } throws exception

        var isExceptionThrown = false
        try {
            objectUnderTest.getUsers().collect()
        } catch (e: NullPointerException) {
            isExceptionThrown = true
        }

        assert(isExceptionThrown)
        coVerify { service.getUsers() }
    }

    @Test
    fun `given a valid response when fetch albums then should return the response`() = runTest {
        val expected = listOf(albumResponse)
        coEvery { service.getAlbums() } returns expected

        objectUnderTest.getAlbums().collect()

        coVerify { service.getAlbums() }
    }

    @Test
    fun `given a exception when fetch albums then should return the exception`() = runTest {
        val exception = NullPointerException()
        coEvery { service.getAlbums() } throws exception

        var isExceptionThrown = false
        try {
            objectUnderTest.getAlbums().collect()
        } catch (e: NullPointerException) {
            isExceptionThrown = true
        }

        assert(isExceptionThrown)
        coVerify { service.getAlbums() }
    }
}