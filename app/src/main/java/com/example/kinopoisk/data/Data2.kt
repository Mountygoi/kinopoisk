package com.example.kinopoisk.data

data class Data2(
    val kinopoiskId: Int,
    val title: String,
    val image: String,
    val genres: List<String>,
    val countries: List<String>
)

data class Genre(
    val genre: String
)
