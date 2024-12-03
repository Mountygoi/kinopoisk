package com.example.kinopoisk.data
data class FilterParams(
    val type: String,
    val yearRange: IntRange?,
    val ratingRange: ClosedFloatingPointRange<Float>,
    val sortOption: String,
    val onlyUnwatched: Boolean,
    val genres: List<String>,
    val countries: List<String>
)