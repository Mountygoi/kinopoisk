package presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.kinopoisk.R
import com.example.kinopoisk.data.Data2
import com.example.kinopoisk.data.FilmResponse
import com.example.kinopoisk.data.ImageResponse
import com.example.kinopoisk.data.NavigationItem
import com.example.kinopoisk.data.SimilarFilmsResponse
import com.example.kinopoisk.data.StaffResponse
import domain.FilmDetailsIntent
import domain.FilmDetailsState
import domain.FilmDetailsViewModel
import domain.SharedViewModel

fun formatDuration(durationInMinutes: Int?): String {
    val hours = durationInMinutes?.div(60) ?: 0
    val minutes = durationInMinutes?.rem(60) ?: 0
    return "$hours ч $minutes мин"
}

@Composable
fun FilmScreen(
    filmDetailsViewModel: FilmDetailsViewModel,
    filmId: Int,
    navController: NavController,
    sharedViewModel: SharedViewModel,
) {
    val state by filmDetailsViewModel.state.collectAsState()
    LaunchedEffect(filmId) {
        filmDetailsViewModel.handleIntent(FilmDetailsIntent.LoadMovieDetails(filmId))
    }

    when (val currentState = state) {
        is FilmDetailsState.Loading -> LoadingView()
        is FilmDetailsState.Error -> {
            ErrorView(currentState.message) {
                filmDetailsViewModel.handleIntent(FilmDetailsIntent.Retry)
            }
        }
        is FilmDetailsState.Success -> {
            FilmDetails(
                film = currentState.film,
                actors = currentState.actors,
                staff = currentState.staff,
                images = currentState.images,
                similarFilms = currentState.similarFilms,
                navController = navController,
                sharedViewModel = sharedViewModel,

                onLikeClicked = { movie ->
                    toggleMovieInList(
                        movie,
                        sharedViewModel.likedMovies.value,
                        sharedViewModel::setLikedMovies
                    )
                },
                onSaveClicked = { movie ->
                    toggleMovieInList(
                        movie,
                        sharedViewModel.savedMovies.value,
                        sharedViewModel::setSavedMovies
                    )
                },
                onWatchClicked = { movie ->
                    toggleMovieInList(
                        movie,
                        sharedViewModel.watchedMovies.value,
                        sharedViewModel::setWatchedMovies
                    )
                },
                onOpenedClicked = { movie ->
                    toggleMovieInList(
                        movie,
                        sharedViewModel.openedMovies.value,
                        sharedViewModel::setOpenedMovies
                    )
                }
            )
        }

        else -> {}
    }
}

fun toggleMovieInList(
    movie: Data2,
    currentList: List<Data2>,
    updateList: (List<Data2>) -> Unit
) {
    if (currentList.contains(movie)) {
        updateList(currentList - movie)
    } else {
        updateList(currentList + movie)
    }
}

@Composable
fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        CircularProgressIndicator()
    }
}
@Composable
fun ErrorView(message: String, onRetry: () -> Unit) {
    Column {
        Text("Error: $message")
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}
@Composable
fun FilmDetails(
    film: FilmResponse,
    actors: List<StaffResponse>?,
    staff: List<StaffResponse>?,
    images: List<ImageResponse>,
    similarFilms: List<SimilarFilmsResponse>,
    navController: NavController,
    sharedViewModel: SharedViewModel,
    onLikeClicked: (Data2) -> Unit,
    onSaveClicked: (Data2) -> Unit,
    onWatchClicked: (Data2) -> Unit,
    onOpenedClicked: (Data2) -> Unit,
)
 {
     val isLiked by sharedViewModel.likedMovies.collectAsState()
     val isSaved by sharedViewModel.savedMovies.collectAsState()
     val isWatched by sharedViewModel.watchedMovies.collectAsState()
     val onOpened by sharedViewModel.openedMovies.collectAsState()

     val scrollState = rememberScrollState()

    val movie = Data2(
        kinopoiskId = film.kinopoiskId,
        title = film.nameRu ?: "Unknown Title",
        image = film.posterUrl ?: "default_image_url",
        genres = film.genres?.map { it.genre } ?: emptyList(),
        countries = film.countries?.map { it.country } ?: emptyList()
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ){
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .padding(top =  4.dp,  start = 20.dp)
        ){
            IconButton(
                onClick = {
                    onOpenedClicked(movie)
                    navController.navigate(NavigationItem.Home.route) {
                    }
                }
            ){
                Icon(
                    painter = painterResource(
                        id = if (onOpened.contains(movie)) R.drawable.caret_left else R.drawable.caret_left),
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(24.dp)
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(480.dp),
            contentAlignment = Alignment.BottomCenter
        ) {


            AsyncImage(
                model = film.posterUrl?.takeIf { it.isNotEmpty() } ?: "default_image_url",
                contentDescription = "Film Poster",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black)
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){

                Text(
                    text = film.nameRu ?: "Нет названия",
                    style = MaterialTheme.typography.h5,
                    color = Color.White,
                    textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = buildString {
                        append("${film.ratingKinopoisk ?: "N/A"} ")
                        append(film.nameRu ?: "Неизвестно")
                        append("\n${film.year ?: "N/A"}, ${film.genres?.joinToString { it.genre } ?: "N/A"}")
                        append("\n${film.countries?.firstOrNull()?.country ?: "N/A"}, ")
                        append("${formatDuration(film.filmLength)}")
                        append(
                            ", ${
                                film.ratingAgeLimits?.filter { it.isDigit() }?.toIntOrNull()
                                    ?.let { "$it+" } ?: "Не указано"
                            }")
                    },
                    style = MaterialTheme.typography.body2,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ){
                    IconButton(
                        onClick = { onLikeClicked(movie) }
                    ){
                        Icon(
                            painter = painterResource(
                                id = if (isLiked.contains(movie)) R.drawable.heart_favorite_save___negative else R.drawable.heart_favorite_save
                            ),
                            contentDescription = "Like",
                            tint = if (isLiked.contains(movie)) Color.Red else Color.White,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                    IconButton(
                        onClick = { onSaveClicked(movie) }
                    ){
                        Icon(
                            painter = painterResource(
                                id = if (isSaved.contains(movie)) R.drawable.favorite_flag_saved___neg else R.drawable.favorite_flag_saved
                            ),
                            contentDescription = "Save",
                            tint = if (isSaved.contains(movie)) Color.Yellow else Color.White,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                    IconButton(
                        onClick = { onWatchClicked(movie) }
                    ){
                        Icon(
                            painter = painterResource(
                                id = if (isWatched.contains(movie)) R.drawable.visible_eye_open else R.drawable.invisible_eye_closed
                            ),
                            contentDescription = "Watched",
                            tint = if (isWatched.contains(movie)) Color.Green else Color.White,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White,
                        )
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More", tint = Color.White,
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = film.shortDescription ?: "Нет краткого описания",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(10.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = film.description ?: "Нет описания",
            modifier = Modifier.padding(10.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (actors != null) {
            ActorLazyGrid(actors = actors, navController = navController)
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (staff != null) {
            StaffLazyGrid(staff = staff)
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (images.isNotEmpty()) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 5.dp)
            ){
                Text(
                    text = "Галерея",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = "К списку >",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(10.dp)
                        .clickable {
                            navController.navigate("gallery/${film.kinopoiskId}")
                        }
                )
            }
            ImageGallery(images = images)
        }
        Spacer(modifier = Modifier.height(16.dp))
        SimilarFilmsRow(similarFilms, navController = navController)
    }
}

@Composable
fun ActorLazyGrid(actors: List<StaffResponse>, navController: NavController) {
    val rows = actors.chunked(4)
    Column(modifier = Modifier.padding(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "В фильме снимались",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "${actors.size} >",
                style = MaterialTheme.typography.h6.copy(color = MaterialTheme.colors.primary),
                modifier = Modifier.clickable { }
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ){
            items(rows) { row ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxHeight()
                ){
                    row.forEach { actor ->
                        Row(
                            modifier = Modifier
                                .padding(4.dp)
                                .clickable {
                                    navController.navigate("actorDetails/${actor.staffId}")
                                }
                        ){
                            AsyncImage(
                                model = actor.posterUrl,
                                contentDescription = "Нет имени",
                                modifier = Modifier
                                    .size(80.dp)
                                    .padding(bottom = 4.dp)
                            )
                            Column {
                                Text(
                                    text = actor.nameRu ?: "Нет имени",
                                    style = MaterialTheme.typography.body2,
                                    maxLines = 1
                                )
                                Text(
                                    text = actor.description ?: "Нет имени",
                                    style = MaterialTheme.typography.body2,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StaffLazyGrid(staff: List<StaffResponse>) {
    val nonActors = staff.filter { it.professionKey != "ACTOR" }
    val rows = nonActors.chunked(2)

    Column(modifier = Modifier.padding(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Над фильмом работали",
                style = MaterialTheme.typography.h6,

                )
            Text(
                text = "${nonActors.size} >",
                style = MaterialTheme.typography.h6.copy(color = MaterialTheme.colors.primary),
                modifier = Modifier.clickable { }
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ){
            items(rows) { row ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxHeight()
                ){
                    row.forEach { staffMember ->
                        Row(modifier = Modifier.padding(4.dp)) {
                            AsyncImage(
                                model = staffMember.posterUrl,
                                contentDescription = staffMember.nameRu ?: "",
                                modifier = Modifier
                                    .size(80.dp)
                                    .padding(bottom = 4.dp)
                            )
                            Column {
                                Text(
                                    text = staffMember.nameRu ?: "",
                                    style = MaterialTheme.typography.body2,
                                    maxLines = 1
                                )
                                Text(
                                    text = staffMember.professionKey ?: "",
                                    style = MaterialTheme.typography.body2,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ImageGallery(images: List<ImageResponse>) {
    if (images.isEmpty()) {
        Text(text = "Галерея пуста", modifier = Modifier.padding(8.dp))
    }else {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ){
            items(images) { image ->
                AsyncImage(
                    model = image.imageUrl,
                    contentDescription = "Image",
                    modifier = Modifier
                        .size(150.dp)
                        .padding(4.dp)
                )
            }
        }
    }
}

@Composable
fun SimilarFilmsRow(similarFilms: List<SimilarFilmsResponse>, navController: NavController) {
    if (similarFilms.isNotEmpty()) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = "Похожие фильмы", style = MaterialTheme.typography.h6)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(similarFilms) { film ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(120.dp)
                    ){
                        AsyncImage(
                            model = film.posterUrl,
                            contentDescription = film.nameRu ?: "",
                            modifier = Modifier
                                .height(180.dp)
                                .width(120.dp)
                                .clickable {
                                    navController.navigate("movieDetails/${film.filmId}")
                                }
                        )
                        Text(
                            text = film.nameRu ?: "",
                            style = MaterialTheme.typography.body2,
                            maxLines = 2,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
