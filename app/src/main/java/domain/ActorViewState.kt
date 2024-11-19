package domain

import com.example.kinopoisk.data.ActorResponse
import com.example.kinopoisk.data.FilmResponse


data class ActorViewState(
    val actor: ActorResponse? = null,
    val films: List<FilmResponse> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

sealed class ActorIntent {
    data class LoadActorDetails(val actorId: Int) : ActorIntent()
    object Retry : ActorIntent()
}