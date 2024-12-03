package domain

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinopoisk.data.Data2
import com.example.kinopoisk.data.KinoPoiskApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MoviesViewM (private val apiService: KinoPoiskApi) : ViewModel() {
    val premiers = mutableStateOf<List<Data2>>(emptyList())
    val popular = mutableStateOf<List<Data2>>(emptyList())
    val top250 = mutableStateOf<List<Data2>>(emptyList())


    init {
        loadMovies("premiers")
        loadMovies("popular")
        loadMovies("top250")
    }


    private fun loadMovies(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = when (category) {
                "premiers" -> apiService.getMovies(yearFrom = 2023)
                "popular" -> apiService.getMovies(order = "NUM_VOTE")
                "top250" -> apiService.getMovies(order = "RATING", ratingFrom = 8)
                else -> null
            }
            val moviesList = response?.body()?.items?.map {
            Data2(
                kinopoiskId = it.kinopoiskId ?: 0,
                title = it.nameRu ?: "Unknown",
                image = it.posterUrl ?: "",
                genres = it.genres?.map { genre -> genre.genre } ?: emptyList(),
                countries = it.countries?.map { country -> country.country } ?: emptyList()
            )
        } ?: emptyList()

            when (category) {
                "premiers" -> premiers.value = moviesList
                "popular" -> popular.value = moviesList
                "top250" -> top250.value = moviesList
            }
        }
    }
}