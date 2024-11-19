package presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.kinopoisk.data.FilmResponse
import domain.ActorFilmographyAction
import domain.ActorFilmographyViewModel



@Composable
fun ActorFilmographyPage(
    actorId: Int,
    viewModel: ActorFilmographyViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(actorId) {
        viewModel.dispatch(ActorFilmographyAction.LoadFilmography(actorId))
    }

    when {
        state.isLoading -> LoadingView()
        state.errorMessage != null -> {
            Text(
                text = state.errorMessage ?: "Неизвестная ошибка",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }
        state.actorData != null -> {
            FilmographyContent(
                actorName = state.actorData?.nameRu ?: "",
                films = state.filmDetails,
                navController = navController
            )
        }
    }
}

@Composable
private fun FilmographyContent(
    actorName: String,
    films: List<FilmResponse>,
    navController: NavController
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Назад",
                    modifier = Modifier
                        .clickable { navController.popBackStack() }
                        .padding(8.dp)
                )
                Text(
                    text = "Фильмография: $actorName",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }

        items(films) { film ->
            FilmRow(
                film = film,
                onFilmClick = { navController.navigate("movieDetails/${film.kinopoiskId}") }
            )
        }
    }
}
@Composable
private fun FilmRow(
    film: FilmResponse,
    onFilmClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onFilmClick)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AsyncImage(
            model = film.posterUrl,
            contentDescription = film.nameRu,
            modifier = Modifier
                .width(80.dp)
                .height(120.dp)
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = film.nameRu ?: "Без названия",
                style = MaterialTheme.typography.titleMedium
            )

            film.ratingKinopoisk?.let {
                Text(
                    text = "Рейтинг: $it",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            film.description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2
                )
            }
        }
    }
}