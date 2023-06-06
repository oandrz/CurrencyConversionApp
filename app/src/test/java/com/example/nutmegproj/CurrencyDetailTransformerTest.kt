package com.example.nutmegproj

import com.example.nutmegproj.data.network.PhotoResponseTransformer
import com.example.nutmegproj.domain.model.Photo
import io.mockk.MockKAnnotations
import org.junit.Before
import org.junit.Test

class CurrencyDetailTransformerTest {

    private lateinit var objectToTest: PhotoResponseTransformer

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        objectToTest = PhotoResponseTransformer()
    }

    @Test
    fun `given responses when transform then should return photo list`() {
        val expected = listOf(
            Photo(
                title = photoResponse.title,
                url = photoResponse.imageUrl,
                userName = userResponse.userName,
                albumName = albumResponse.title
            )
        )
        val result = objectToTest.transform(
            listOf(photoResponse),
            listOf(albumResponse),
            listOf(userResponse),
        )

        assert(expected == result)
    }
}