package presentation
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.kinopoisk.data.ActorResponse
import com.example.kinopoisk.data.FilmResponse
import domain.ActorIntent
import domain.ActorViewModel




@Composable
fun ActorPagee(viewModel: ActorViewModel, actorId: Int, navController: NavController) {
    val state by viewModel.state


    LaunchedEffect(actorId) {
        viewModel.handleIntent(ActorIntent.LoadActorDetails(actorId))
    }


    when {
        state.isLoading -> LoadingView()
        state.errorMessage != null -> {
            Text(
                text = state.errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }
        state.actor != null -> {
            ActorDetails(
                actor = state.actor!!,
                films = state.films,
                navController = navController
            )
        }
    }
}




@Composable
fun ActorDetails(
    actor: ActorResponse,
    films: List<FilmResponse>,
    navController: NavController
) {
    val filmographyCount = films.size
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Назад",
                modifier = Modifier
                    .clickable { navController.popBackStack() }
                    .padding(bottom = 16.dp)
            )
            Row {
                AsyncImage(
                    model = actor.posterUrl,
                    contentDescription = actor.nameRu ?: "Нет имени",
                    modifier = Modifier
                        .width(146.dp)
                        .height(201.dp)
                )
                Column(
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Text(
                        text = actor.nameRu ?: "",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Text(
                        text = actor.profession ?: "не указан профессия",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }


        item {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical =  8.dp)
            ) {
                Text(
                    text = "Лучшее",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Все",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable { }


                )
            }
        }
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                items(
                    items = films.filter { it.ratingKinopoisk != null && it.ratingKinopoisk > 7.0 }
                        .sortedByDescending { it.ratingKinopoisk }
                ) { film ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(120.dp)
                    ) {
                        AsyncImage(
                            model = film.posterUrl,
                            contentDescription = film.nameRu ?: "Нет названия",
                            modifier = Modifier
                                .height(156.dp)
                                .width(111.dp)
                        )
                        Text(
                            text = film.nameRu ?: "Нет названия",
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2,
                        )
                    }
                }
                item {
                    Column(
                        modifier = Modifier,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        IconButton(
                            onClick = {},
                            modifier = Modifier
                                .size(48.dp)
                                .border(
                                    BorderStroke(1.dp, Color.White),
                                    shape = CircleShape
                                )


                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Next",
                                tint = Color(0xFF3D3BFF)
                            )
                        }
                        Text(
                            text = "Показать все",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black,
                            modifier = Modifier.clickable {}
                        )
                    }
                }
            }
        }


        item {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = "Фильмография",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "К списку",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.clickable {
                        navController.navigate("actorFilmography/${actor.personId}") // Navigate with actor ID
                    }
                        .padding(horizontal = 8.dp)
                )


            }
            Text(
                text = "$filmographyCount фильма",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF838391),
            )
        }


        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                items(films) { film ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = film.posterUrl,
                            contentDescription = film.nameRu ?: "Нет названия",
                            modifier = Modifier
                                .height(156.dp)
                                .width(111.dp)
                        )
                        Text(
                            text = film.nameRu ?: "Нет названия",
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2
                        )


                    }
                }
                item {
                    Column(
                        modifier = Modifier,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        IconButton(
                            onClick = {},
                            modifier = Modifier
                                .size(48.dp)
                                .border(
                                    BorderStroke(1.dp, Color.White),
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Next",
                                tint = Color(0xFF3D3BFF)
                            )
                        }
                        Text(
                            text = "Показать все",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black,
                            modifier = Modifier.clickable {}
                        )
                    }
                }
            }
        }
    }
}