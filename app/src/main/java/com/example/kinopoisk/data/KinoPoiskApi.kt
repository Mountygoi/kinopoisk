package com.example.kinopoisk.data

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface KinoPoiskApi {
    @GET("api/v2.2/films")
    suspend fun getMovies(
        @Query("order") order: String = "NUM_VOTE",
        @Query("type") type: String = "FILM",
        @Query("ratingFrom") ratingFrom: Int = 8,
        @Query("ratingTo") ratingTo: Int = 10,
        @Query("yearFrom") yearFrom: Int = 1900,
        @Query("yearTo") yearTo: Int = 2100,
        @Query("page") page: Int = 1,
        @Header("X-API-KEY") apiKey: String = "608b8a7b-81d8-44e1-adf9-7391d52ae658"
    ): Response<MovieResponse>

    @GET("api/v2.2/films/{id}")
    suspend fun getFilmById(
        @Path("id") id: Int,
        @Header("X-API-KEY") apiKey: String = "608b8a7b-81d8-44e1-adf9-7391d52ae658"
    ): Response<FilmResponse>

    @GET("api/v1/staff")
    suspend fun getActorsByFilmId(
        @Query("filmId") filmId: Int,
        @Header("X-API-KEY") apiKey: String = "608b8a7b-81d8-44e1-adf9-7391d52ae658"
    ): Response<List<StaffResponse>>

    @GET("api/v1/staff")
    suspend fun getStaffByFilmId(
        @Query("filmId") filmId: Int,
        @Header("X-API-KEY") apiKey: String = "608b8a7b-81d8-44e1-adf9-7391d52ae658"
    ): Response<List<StaffResponse>>

    @GET("api/v2.2/films/{id}/similars")
    suspend fun getSimilarByFilmId(
        @Path("id") id:Int,
        @Header("X-API-KEY") apiKey: String = "608b8a7b-81d8-44e1-adf9-7391d52ae658"
    ):Response<SimilarResponse>

    @GET("api/v1/staff/{id}")
    suspend fun getActorDetailById(
        @Path("id") id: Int,
        @Header("X-API-KEY") apiKey: String = "608b8a7b-81d8-44e1-adf9-7391d52ae658"
    ): Response<ActorResponse>

    @GET("api/v2.2/films/{id}/images")
    suspend fun getFilmImages(
        @Path("id") id: Int,
        @Header("X-API-KEY") apiKey: String = "608b8a7b-81d8-44e1-adf9-7391d52ae658"
    ): Response<FilmImagesResponse>

    @GET("api/v2.1/films/search-by-keyword")
    suspend fun searchFilms(
        @Query("keyword") keyword: String,
        @Header("X-API-KEY") apiKey: String = "310642af-0077-49b4-b6e3-a21974d8f028"
    ): Response<MovieResponsee>

    @GET("api/v2.2/films")
    suspend fun getMovies(
        @Query("order") order: String = "NUM_VOTE",
        @Query("type") type: String = "ALL",
        @Query("ratingFrom") ratingFrom: Int = 0,
        @Query("ratingTo") ratingTo: Int = 10,
        @Query("yearFrom") yearFrom: Int = 1900,
        @Query("yearTo") yearTo: Int = 2100,
        @Query("genre") genre: String? = null,
        @Query("country") country: String? = null,
        @Query("page") page: Int = 1,
        @Header("X-API-KEY") apiKey: String = "310642af-0077-49b4-b6e3-a21974d8f028"
    ): Response<MovieResponse>


    @GET("api/v2.2/films/filters")
    suspend fun getFilters(
        @Header("X-API-KEY") apiKey: String = "310642af-0077-49b4-b6e3-a21974d8f028"
    ): Response<FiltersResponse>

    // Новый дата класс для ответа
    data class FiltersResponse(
        val genres: List<Genre>,
        val countries: List<Country>
    )

}

data class MovieResponsee(
    val total: Int,
    val totalPages: Int,
    val items: List<Movie>

)

val retrofit = Retrofit.Builder()
    .baseUrl("https://kinopoiskapiunofficial.tech/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()


val apiService = retrofit.create(KinoPoiskApi::class.java)
