package domain

import com.example.kinopoisk.data.ActorResponse
import com.example.kinopoisk.data.FilmResponse

data class ActorFilmographyState(
    val actorData: ActorResponse? = null,
    val filmDetails: List<FilmResponse> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed class ActorFilmographyAction {
    data class LoadFilmography(val actorId: Int) : ActorFilmographyAction()
    data class SetActorData(val actorData: ActorResponse) : ActorFilmographyAction()
    data class SetFilmDetails(val filmDetails: List<FilmResponse>) : ActorFilmographyAction()
    data class SetError(val errorMessage: String) : ActorFilmographyAction()
    object SetLoading : ActorFilmographyAction()
}