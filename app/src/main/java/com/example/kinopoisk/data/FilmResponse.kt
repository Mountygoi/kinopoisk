package com.example.kinopoisk.data

data class FilmResponse(
    val kinopoiskId:Int,
    val posterUrl:String?,
    val nameRu:String?,
    val ratingKinopoisk:Double?,
    val year:Int?,
    val genres : List<Genre>?,
    val countries: List<Country>?,
    val filmLength:Int?,
    val ratingAgeLimits:String?,
    val shortDescription:String?,
    val description:String?,
)

data class SimilarResponse(
    val total:Int,
    val items: List<SimilarFilmsResponse>
)
data class SimilarFilmsResponse(
    val filmId: Int,
    val nameRu: String,
    val posterUrl: String
)