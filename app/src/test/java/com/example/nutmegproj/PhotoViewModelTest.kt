package com.example.nutmegproj

import com.example.nutmegproj.domain.GetPhotoListUseCase
import com.example.nutmegproj.domain.model.Photo
import com.example.nutmegproj.presentation.photolist.ListUIState
import com.example.nutmegproj.presentation.photolist.ListViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class PhotoViewModelTest {

    @RelaxedMockK
    private lateinit var usecase: GetPhotoListUseCase

    private lateinit var vm: ListViewModel

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        vm = ListViewModel(usecase)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `given success response when get photo then should return list of photo`() = runTest {
        val photoList = listOf(
            Photo(
                title = photoResponse.title,
                url = photoResponse.imageUrl,
                userName = userResponse.userName,
                albumName = albumResponse.title
            )
        )

        coEvery { usecase.run() } returns flowOf(photoList)

        vm.getPhotoList()

        assert(vm.uiState.value is ListUIState.Success)
        coVerify { usecase.run() }
    }
}