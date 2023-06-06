package com.example.nutmegproj

import com.example.nutmegproj.data.network.response.AlbumResponse
import com.example.nutmegproj.data.network.response.PhotoResponse
import com.example.nutmegproj.data.network.response.UserResponse

val photoResponse = PhotoResponse(
    albumId = 1,
    id = 1,
    title = "photo test",
    imageUrl = "https image url",
    thumbnailUrl = "thumbnail url"
)

val userResponse = UserResponse(
    userId = 1,
    userName = "test"
)

val albumResponse = AlbumResponse(
    userId = 1,
    id = 1,
    title = "testing album"
)