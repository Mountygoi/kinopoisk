package presentation

import MovieItem
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.kinopoisk.data.Data2


@Composable
fun MoviesGrid(movies: List<Data2>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(movies) { movie ->
            MovieItem(movie)
        }
    }
}
