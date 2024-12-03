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
        @Header("X-API-KEY") apiKey: String = "5244845c-3dfc-444b-ae3f-80a8d8dca96d"
    ): Response<MovieResponse>

    @GET("api/v2.2/films/{id}")
    suspend fun getFilmById(
        @Path("id") id: Int,
        @Header("X-API-KEY") apiKey: String = "5244845c-3dfc-444b-ae3f-80a8d8dca96d"
    ): Response<FilmResponse>

    @GET("api/v1/staff")
    suspend fun getActorsByFilmId(
        @Query("filmId") filmId: Int,
        @Header("X-API-KEY") apiKey: String = "5244845c-3dfc-444b-ae3f-80a8d8dca96d"
    ): Response<List<StaffResponse>>

    @GET("api/v1/staff")
    suspend fun getStaffByFilmId(
        @Query("filmId") filmId: Int,
        @Header("X-API-KEY") apiKey: String = "5244845c-3dfc-444b-ae3f-80a8d8dca96d"
    ): Response<List<StaffResponse>>

    @GET("api/v2.2/films/{id}/similars")
    suspend fun getSimilarByFilmId(
        @Path("id") id:Int,
        @Header("X-API-KEY") apiKey: String = "5244845c-3dfc-444b-ae3f-80a8d8dca96d"
    ):Response<SimilarResponse>

    @GET("api/v1/staff/{id}")
    suspend fun getActorDetailById(
        @Path("id") id: Int,
        @Header("X-API-KEY") apiKey: String = "5244845c-3dfc-444b-ae3f-80a8d8dca96d"
    ): Response<ActorResponse>

    @GET("api/v2.2/films/{id}/images")
    suspend fun getFilmImages(
        @Path("id") id: Int,
        @Header("X-API-KEY") apiKey: String = "5244845c-3dfc-444b-ae3f-80a8d8dca96d"
    ): Response<FilmImagesResponse>

    @GET("api/v2.1/films/search-by-keyword")
    suspend fun  searchFilms(
        @Query("keyword") query: String,
        @Header("X-API-KEY") apiKey: String = "5244845c-3dfc-444b-ae3f-80a8d8dca96d"
    ): Response<MovieSearchResponse>
}

val retrofit = Retrofit.Builder()
    .baseUrl("https://kinopoiskapiunofficial.tech/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()


val apiService = retrofit.create(KinoPoiskApi::class.java)
