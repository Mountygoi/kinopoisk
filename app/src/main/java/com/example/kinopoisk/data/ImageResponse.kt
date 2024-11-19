package com.example.kinopoisk.data

data class ImageResponse(
    val imageUrl: String
)

data class FilmImagesResponse(
    val items: List<ImageResponse>
)
