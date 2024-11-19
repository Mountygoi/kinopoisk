package com.example.kinopoisk.data

class GalleryRepository {
    suspend fun fetchFilmImages(filmId: Int): List<ImageResponse>? {
        val response = apiService.getFilmImages(filmId)
        if (response.isSuccessful) {
            val items = response.body()?.items
            println("Fetched Images: $items")
            return items
        } else {
            println("Error: ${response.errorBody()?.string()}")
            return null
        }
    }
}