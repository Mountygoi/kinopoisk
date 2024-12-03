package domain

import com.example.kinopoisk.data.Data2

sealed interface ScreenState {
    data object Initial : ScreenState
    data object Loading : ScreenState
    data class Success(val movies: List<Data2>) : ScreenState
    data class Error(val message: String) : ScreenState
}
