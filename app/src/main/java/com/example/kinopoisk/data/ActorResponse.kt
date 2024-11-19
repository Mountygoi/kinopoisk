package com.example.kinopoisk.data

data class ActorResponse(
    val personId: Int,
    val nameRu: String?,
    val posterUrl: String?,
    val profession: String?,
    val facts: List<String>?,
    val films: List<ActorFilmography>
)
data class ActorFilmography(
    val filmId: Int,
    val nameRu: String?,
    val rating: Double?,
    val description: String?,
    val posterUrl: String?,
    val professionKey: String?
)