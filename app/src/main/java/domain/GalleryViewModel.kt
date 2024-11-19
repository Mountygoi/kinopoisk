package domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinopoisk.data.GalleryRepository
import com.example.kinopoisk.data.ImageResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class GalleryIntent {
    data class FetchGalleryData(val filmId: Int) : GalleryIntent()
}
class GalleryViewModel : ViewModel() {
    private val repository = GalleryRepository()

    private val _filmImages = MutableStateFlow<List<ImageResponse>>(emptyList())
    val filmImages: StateFlow<List<ImageResponse>> = _filmImages.asStateFlow()

    fun handleIntent(intent: GalleryIntent) {
        when (intent) {
            is GalleryIntent.FetchGalleryData -> fetchFilmData(intent.filmId)
        }
    }

    private fun fetchFilmData(filmId: Int) {
        viewModelScope.launch {
            val images = repository.fetchFilmImages(filmId)
            _filmImages.value = images ?: emptyList()
        }
    }
}