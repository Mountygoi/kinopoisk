package domain

import com.example.kinopoisk.data.Data2

sealed interface ScreenState {
    object Initial : ScreenState
    object Loading : ScreenState
    data class Success(val movies: List<Data2>) : ScreenState
    data class Error(val message: String) : ScreenState
}
