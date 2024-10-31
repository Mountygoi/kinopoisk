package com.example.kinopoisk.data

data class MovieResponse(
    val total: Int,
    val totalPages: Int,
    val items: List<Movie>
)
