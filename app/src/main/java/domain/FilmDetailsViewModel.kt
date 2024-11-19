package domain


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.kinopoisk.data.KinoPoiskApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



class FilmDetailsViewModel(
    private val apiService: KinoPoiskApi
) : ViewModel() {
    private val _state = MutableStateFlow<FilmDetailsState>(FilmDetailsState.Loading)
    val state: StateFlow<FilmDetailsState> = _state.asStateFlow()

    fun handleIntent(intent: FilmDetailsIntent) {
        when (intent) {
            is FilmDetailsIntent.LoadMovieDetails -> loadMovieDetails(intent.filmId)
            FilmDetailsIntent.Retry -> retryLoading()
        }
    }

    private fun loadMovieDetails(filmId: Int) {
        _state.value = FilmDetailsState.Loading

        viewModelScope.launch {
            try {
                val filmResponse = withContext(Dispatchers.IO) {
                    apiService.getFilmById(filmId)
                }
                val actorsResponse = withContext(Dispatchers.IO) {
                    apiService.getActorsByFilmId(filmId)
                }
                val staffResponse = withContext(Dispatchers.IO) {
                    apiService.getStaffByFilmId(filmId)
                }
                val imagesResponse = withContext(Dispatchers.IO) {
                    apiService.getFilmImages(filmId)
                }
                val similarFilmsResponse = withContext(Dispatchers.IO) {
                    apiService.getSimilarByFilmId(filmId)
                }

                if (filmResponse.isSuccessful) {
                    val film = filmResponse.body()
                    if (film != null) {
                        _state.value = FilmDetailsState.Success(
                            film = film,
                            actors = actorsResponse.body()?.filter { it.professionKey == "ACTOR" },
                            staff = staffResponse.body() ?: emptyList(),
                            images = imagesResponse.body()?.items ?: emptyList(),
                            similarFilms = similarFilmsResponse.body()?.items ?: emptyList()
                        )
                    } else {
                        _state.value = FilmDetailsState.Error("Film data is null")
                    }
                } else {
                    _state.value =
                        FilmDetailsState.Error("Failed to load movie details: ${filmResponse.code()}")
                }
            } catch (e: Exception) {
                _state.value = FilmDetailsState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun retryLoading() {
    }
}



class FilmDetailsViewModelFactory(
    private val apiService: KinoPoiskApi
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilmDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FilmDetailsViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}