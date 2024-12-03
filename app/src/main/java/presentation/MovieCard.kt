package presentation

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.kinopoisk.data.Data2
import domain.MoviesViewM


@Composable
fun MovieCard(item: Data2, navController: NavController) {
    Column(
        modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp)
            .width(140.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = item.image,
            contentDescription = item.title,
            modifier = Modifier.size(150.dp)
                .clickable {
                    if (item.kinopoiskId != 0) {
                        navController.navigate("movieDetails/${item.kinopoiskId}")
                    } else {
                        Log.e("NavigationError", "Invalid kinopoiskId for movie: ${item.title}")
                    }

                }
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = item.title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
        )
        Spacer(modifier = Modifier.height(5.dp))
        val genresText = if (item.genres.isNotEmpty()) item.genres.first() else "null"
        Text(
            text = genresText,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 5.dp)
        )
    }
}


@Composable
fun MovieDetailsScreen(filmTitle: String?,item: Data2) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Movie Details", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))

        filmTitle?.let {
            Text(text = "Title: $filmTitle", style = MaterialTheme.typography.headlineMedium)
            AsyncImage(
                model = item.image,
                contentDescription = item.title,
                modifier = Modifier.size(120.dp))
        } ?: Text(text = "Movie details not available.")
    }
}
@Composable
fun MyLazyRow(title: String, movies: List<Data2>, navController:NavHostController, category:String){
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth()
        ){
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
            )
            Text(
                text = "Все",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Blue,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable {
                        navController.navigate("details/$category")
                    }
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            items(movies.take(8)) { movie ->
                MovieCard(movie, navController)
            }
            item {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    IconButton(
                        onClick = {
                            navController.navigate("details/$category")
                        },
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
                        modifier = Modifier.clickable {
                            navController.navigate("details/$category")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MainPage(navController: NavHostController,viewM: MoviesViewM){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.Top
    ){
        Text(
            text = "SkillCinema",
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 35.dp)
                .align(Alignment.Start),
            color = Color.Black
        )
        LazyColumn{
            item{ MyLazyRow("Премьеры", viewM.premiers.value,navController,"premiers") }
            item{ MyLazyRow("Популярные", viewM.popular.value,navController,"popular") }
            item{ MyLazyRow("Топ-250", viewM.top250.value,navController,"top250") }
        }
    }
}
