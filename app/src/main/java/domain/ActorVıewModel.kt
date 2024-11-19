package domain
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.kinopoisk.data.KinoPoiskApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ActorViewModel(private val apiService: KinoPoiskApi) {
    private val _state = mutableStateOf(ActorViewState())
    val state: State<ActorViewState> get() = _state


    fun handleIntent(intent: ActorIntent) {
        when (intent) {
            is ActorIntent.LoadActorDetails -> loadActorDetails(intent.actorId)
            ActorIntent.Retry -> retry()
        }
    }


    private fun loadActorDetails(actorId: Int) {
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getActorDetailById(actorId)
                if (response.isSuccessful) {
                    val actorData = response.body()
                    val filmDetails = actorData?.films?.let { films ->
                        films.mapNotNull { film ->
                            val filmResponse = apiService.getFilmById(film.filmId)
                            if (filmResponse.isSuccessful) filmResponse.body() else null
                        }
                    } ?: emptyList()
                    _state.value = ActorViewState(
                        actor = actorData,
                        films = filmDetails,
                        isLoading = false
                    )
                } else {
                    _state.value = _state.value.copy(
                        errorMessage = "Ошибка загрузки: ${response.code()}",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    errorMessage = "Сетевая ошибка: ${e.message}",
                    isLoading = false
                )
            }
        }
    }


    private fun retry() {
        val actorId = _state.value.actor?.personId ?: return
        loadActorDetails(actorId)
    }
}