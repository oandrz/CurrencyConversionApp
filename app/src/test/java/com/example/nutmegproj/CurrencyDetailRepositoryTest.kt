package com.example.nutmegproj

import com.example.nutmegproj.data.PhotoRepository
import com.example.nutmegproj.data.PhotoRepositoryImpl
import com.example.nutmegproj.data.network.CurrencyRemoteDataSource
import com.example.nutmegproj.data.network.PhotoResponseTransformer
import com.example.nutmegproj.domain.model.Photo
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyDetailRepositoryTest {

    @MockK
    private lateinit var remoteDataSource: CurrencyRemoteDataSource

    @MockK
    private lateinit var transformer: PhotoResponseTransformer

    private lateinit var repository: PhotoRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repository = PhotoRepositoryImpl(
            remoteDataStore = remoteDataSource,
            photoResponseTransformer = transformer
        )
    }

    @Test
    fun `given success response when get photo then should return list of photo`() = runTest {
        val photoResponseList = listOf(photoResponse)
        val userResponseList = listOf(userResponse)
        val albumResponseList = listOf(albumResponse)
        val photoList = listOf(
            Photo(
                title = photoResponse.title,
                url = photoResponse.imageUrl,
                userName = userResponse.userName,
                albumName = albumResponse.title
            )
        )

        coEvery { remoteDataSource.getPhotos() } returns flowOf(photoResponseList)
        coEvery { remoteDataSource.getUsers() } returns flowOf(userResponseList)
        coEvery { remoteDataSource.getAlbums() } returns flowOf(albumResponseList)
        coEvery { transformer.transform(photoResponseList, albumResponseList, userResponseList) } returns photoList

        repository.getPhotoList().collect {
            assert(it == photoList)
        }

        coVerify { remoteDataSource.getPhotos() }
        coVerify { remoteDataSource.getUsers() }
        coVerify { remoteDataSource.getAlbums() }
        coVerify { transformer.transform(photoResponseList, albumResponseList, userResponseList) }
    }

    @Test
    fun `given failure response when get photo then should return exception`() = runTest {
        val photoResponseList = listOf(photoResponse)
        val userResponseList = listOf(userResponse)
        val albumResponseList = listOf(albumResponse)
        val photoList = listOf(
            Photo(
                title = photoResponse.title,
                url = photoResponse.imageUrl,
                userName = userResponse.userName,
                albumName = albumResponse.title
            )
        )

        val exception = NullPointerException()

        coEvery { remoteDataSource.getPhotos() } returns flowOf(photoResponseList)
        coEvery { remoteDataSource.getUsers() } throws exception
        coEvery { remoteDataSource.getAlbums() } returns flowOf(albumResponseList)
        coEvery { transformer.transform(photoResponseList, albumResponseList, userResponseList) } returns photoList

        var isExceptionThrown = false
        try {
            repository.getPhotoList()
        } catch (e: NullPointerException) {
            isExceptionThrown = true
        }

        assert(isExceptionThrown)
        coVerify { remoteDataSource.getUsers() }
        coVerify(exactly = 0) { remoteDataSource.getPhotos() }
        coVerify(exactly = 0) { remoteDataSource.getAlbums() }
        coVerify(exactly = 0) { transformer.transform(photoResponseList, albumResponseList, userResponseList) }
    }
}