package com.example.kinopoisk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.kinopoisk.ui.theme.BottomNavigationBar
import com.example.kinopoisk.ui.theme.SetupNavigation


data class Data2(val title: String, val image: Int)

fun createMovieList(repeat: Int): List<Data2>{
    return List(repeat) {
        Data2 ("Близкие", R.drawable.a4__1)
    }
}

val premiers = createMovieList(8)
val popular  = createMovieList(8)
val actionMovies   = createMovieList(8)
val top250 = createMovieList(8)
val dramaFrance = createMovieList(8)
val serials = createMovieList(8)

@Composable
fun MovieCard(item: Data2) {
    Column(
        modifier = Modifier.padding(horizontal = 8.dp, vertical  = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Card(
            modifier = Modifier.size(160.dp, 120.dp)
                .clip(RoundedCornerShape(4.dp))
        ){
            Image(
                painter = painterResource(id = item.image),
                contentDescription = item.title,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = item.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 5.dp)
                .align(Alignment.Start))
    }
}

@Composable
fun MyLazyRow(title: String, movies: List<Data2>) {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth()
                .fillMaxWidth()
        ) {
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
                modifier = Modifier.padding(10.dp)
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            items(movies) { movie ->
                MovieCard(item = movie)
            }
            item{
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Image(
                        painter = painterResource(id = R.drawable.newphoto),
                        contentDescription = null,
                        modifier = Modifier.size(120.dp)
                            .padding(15.dp)
                        )
                    Text(
                        text = "Показать все",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}
@Composable
fun MainPage(){
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.Top
    ){
        Text(
            text = "SkillCinema",
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(vertical = 35.dp, horizontal = 15.dp)
                .align(Alignment.Start),
            color = Color.Black
        )
        LazyColumn {
            item{ MyLazyRow("Премьеры", premiers)}
            item{ MyLazyRow("Популярное", popular)}
            item{ MyLazyRow("Боевики США ", actionMovies)}
            item{ MyLazyRow("Топ-250", top250)}
            item{ MyLazyRow("Драмы Франции", dramaFrance)}
            item{ MyLazyRow("Сериалы", serials)}

        }
    }
}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                MainPage()

        }
    }
}



