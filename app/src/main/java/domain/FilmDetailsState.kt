package domain


import com.example.kinopoisk.data.FilmResponse
import com.example.kinopoisk.data.ImageResponse
import com.example.kinopoisk.data.SimilarFilmsResponse
import com.example.kinopoisk.data.StaffResponse


sealed class FilmDetailsState {
    object Loading : FilmDetailsState()
    data class Success(
        val film: FilmResponse,
        val actors: List<StaffResponse>?,
        val staff: List<StaffResponse>?,
        val images: List<ImageResponse>,
        val similarFilms: List<SimilarFilmsResponse>
    ) : FilmDetailsState()

    data class Error(val message: String) : FilmDetailsState()
}

sealed class FilmDetailsIntent {
    data class LoadMovieDetails(val filmId: Int) : FilmDetailsIntent()
    object Retry : FilmDetailsIntent()
}