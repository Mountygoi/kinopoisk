package presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.kinopoisk.R
import com.example.kinopoisk.data.FilterParams
import com.example.kinopoisk.data.KinoPoiskApi
import com.example.kinopoisk.data.Movie


@Composable
fun SearchBar(query: String, onQueryChanged: (String) -> Unit,navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChanged,
            label = { Text("Название Фильмов") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Фильмы",
                    tint = Color.Gray
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            trailingIcon = {
                Image(painter = painterResource(id = R.drawable.icon), contentDescription = "", modifier = Modifier
                    .size(24.dp)
                    .clickable { navController.navigate("filterscreen") }
                )
            },shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}
@Composable
fun SearchScreen(apiService: KinoPoiskApi, navController: NavController) {
    var query by remember { mutableStateOf("") }
    val movies = remember { mutableStateOf<List<Movie>>(emptyList()) }
    var filters by remember { mutableStateOf<FilterParams?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(filters, query) {
        if (query.isNotEmpty()) {
            isLoading = true
            try {
                val response = apiService.searchFilms(query)
                if (response.isSuccessful) {
                    val allMovies = response.body()?.films ?: emptyList()
                    Log.d("API Response", response.toString())
                    movies.value = allMovies.filter { movie ->
                        movie.nameRu?.contains(query, ignoreCase = true)   ==true
                    }
                }else {
                    errorMessage = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = "Network error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        SearchBar(query = query, onQueryChanged = { query = it },navController=navController)

        when {
            isLoading -> {
                CircularProgressIndicator()
            }
            errorMessage.isNotEmpty() -> {
                Text(text = errorMessage, color = Color.Red)
            }
            movies.value.isNotEmpty() -> {
                LazyColumn {
                    items(movies.value) { movie ->
                        MovieCardd(movie = movie)
                    }
                }
            }
            query.isNotEmpty() && movies.value.isEmpty() -> {
                Text("Ничего не найдено по запросу \"$query\".", modifier = Modifier.padding(16.dp))
            }
        }
    }
}


@Composable
fun MovieCardd(movie: Movie) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = movie.posterUrl,
                contentDescription = "Poster",
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = movie.nameRu ?: "Без названия",
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Год: ${movie.year ?: "Не указан"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Жанры: ${movie.genres?.joinToString(", ") { it.genre } ?: "Не указаны"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )

                // Выводим страны
                Text(
                    text = "Страны: ${movie.countries?.joinToString(", ") { it.country } ?: "Не указаны"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}