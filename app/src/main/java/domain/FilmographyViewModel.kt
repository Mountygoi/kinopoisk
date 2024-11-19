package domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.kinopoisk.data.KinoPoiskApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActorFilmographyViewModel(private val apiService: KinoPoiskApi) : ViewModel() {
    private val _state = MutableStateFlow(ActorFilmographyState())
    val state: StateFlow<ActorFilmographyState> get() = _state

    fun dispatch(action: ActorFilmographyAction) {
        when (action) {
            is ActorFilmographyAction.LoadFilmography -> loadFilmography(action.actorId)
            is ActorFilmographyAction.SetActorData -> _state.value = _state.value.copy(actorData = action.actorData, isLoading = false)
            is ActorFilmographyAction.SetFilmDetails -> _state.value = _state.value.copy(filmDetails = action.filmDetails, isLoading = false)
            is ActorFilmographyAction.SetError -> _state.value = _state.value.copy(errorMessage = action.errorMessage, isLoading = false)
            ActorFilmographyAction.SetLoading -> _state.value = _state.value.copy(isLoading = true)
        }
    }
    private fun loadFilmography(actorId: Int) {
        viewModelScope.launch {
            dispatch(ActorFilmographyAction.SetLoading)
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.getActorDetailById(actorId)
                }
                if (response.isSuccessful) {
                    response.body()?.let { actorData ->
                        dispatch(ActorFilmographyAction.SetActorData(actorData))
                        val filmDetails = withContext(Dispatchers.IO) {
                            actorData.films.map { film ->
                                async {
                                    val filmResponse = apiService.getFilmById(film.filmId)
                                    if (filmResponse.isSuccessful) {
                                        filmResponse.body()
                                    } else null
                                }
                            }.awaitAll().filterNotNull().distinctBy { it.kinopoiskId }
                        }
                        dispatch(ActorFilmographyAction.SetFilmDetails(filmDetails))
                    }
                } else {
                    dispatch(ActorFilmographyAction.SetError("Ошибка загрузки: ${response.code()}"))
                }
            } catch (e: Exception) {
                dispatch(ActorFilmographyAction.SetError("Сетевая ошибка: ${e.message}"))
            }
        }
    }
}

class ActorFilmographyViewModelFactory(private val apiService: KinoPoiskApi) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActorFilmographyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ActorFilmographyViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}