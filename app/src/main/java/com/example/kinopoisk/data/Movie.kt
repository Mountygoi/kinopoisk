package com.example.kinopoisk.data

data class Movie(
    val kinopoiskId: Int,
    val nameRu: String? = null,
    val posterUrl: String? = null,
    val genres: List<Genre>?  = null,
    val countries: List<Country>? = null
)

data class MovieSearchResponse(
    val films: List<Movie>
)