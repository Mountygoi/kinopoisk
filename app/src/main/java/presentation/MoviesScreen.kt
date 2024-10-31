import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.example.kinopoisk.data.KinoPoiskApi
import com.example.kinopoisk.ui.theme.Data2
import java.lang.reflect.Modifier

@Composable
fun MoviesScreen(apiService: KinoPoiskApi, category: String) {
    var screenState by remember { mutableStateOf<ScreenState>(ScreenState.Initial) }
    val movies = remember { mutableStateOf<List<Data2>>(emptyList()) }


    LaunchedEffect(category) {
        screenState = ScreenState.Loading
        try {
            val response = when (category) {
                "premiers" -> apiService.getMovies(yearFrom = 2023)
                "popular" -> apiService.getMovies(order = "NUM_VOTE")
                "top250" -> apiService.getMovies(order = "RATING", ratingFrom = 8)
                else -> null
            }


            if (response?.isSuccessful == true) {
                val movieList = response.body()?.items?.map {
                    Data2(
                        title = it.nameRu ?: "Unknown",
                        image = it.posterUrl ?: "",
                        genres = it.genres?.map { genre -> genre.genre } ?: emptyList(),
                        countries = it.countries?.map { country -> country.country } ?: emptyList()
                    )
                } ?: emptyList()


                movies.value = movieList
                screenState = ScreenState.Success(movies = movieList)
            } else {
                screenState = ScreenState.Error("Error loading movies: ${response?.code()}")
            }
        } catch (e: Exception) {
            screenState = ScreenState.Error("Network error: ${e.message}")
        }
    }


    when (screenState) {
        is ScreenState.Initial -> {
            Text("Welcome! Tap to load movies.", modifier = Modifier.padding(16.dp))
        }

        is ScreenState.Loading -> {
            Text("Loading...", modifier = Modifier.padding(16.dp))
        }

        is ScreenState.Success -> {
            MoviesGrid(movies = movies.value)
        }

        is ScreenState.Error -> {
            Text(
                text = (screenState as ScreenState.Error).message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }


        else -> {}
    }
}