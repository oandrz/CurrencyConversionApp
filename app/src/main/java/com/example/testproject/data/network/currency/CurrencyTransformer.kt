package com.example.testproject.data.network.currency

import com.example.testproject.data.network.currency.response.AlbumResponse
import com.example.testproject.data.network.currency.response.CurrencyResponse
import com.example.testproject.data.network.currency.response.UserResponse
import com.example.testproject.domain.model.Photo
import javax.inject.Inject

class CurrencyTransformer @Inject constructor() {

    fun transform(
        photoResponses: List<CurrencyResponse>,
        albumResponses: List<AlbumResponse>,
        userResponses: List<UserResponse>
    ): List<Photo> {
        val userAlbum = albumResponses.groupBy { it.userId }
        val photoList = mutableListOf<Photo>()
        userResponses.forEach { user ->
            val albumOfUser = userAlbum[user.userId]
            albumOfUser?.let {
                photoList.addAll(
                    it.map { album ->
                        val photoResponse = photoResponses.first { photoResponse ->  photoResponse.albumId == album.id }
                        Photo(
                            title = photoResponse.title,
                            url = photoResponse.imageUrl,
                            userName = user.userName,
                            albumName = album.title
                        )
                    }
                )
            }
        }
        return photoList
    }
}