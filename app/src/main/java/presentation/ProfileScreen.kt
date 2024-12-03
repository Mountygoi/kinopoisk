package presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kinopoisk.R
import com.example.kinopoisk.data.Data2
import domain.SharedViewModel


@Composable
    fun ProfileScreen(
    navController: NavController,
    viewModel: SharedViewModel
) {
        val watchedMovies = viewModel.watchedMovies.collectAsState().value
        val openedMovies = viewModel.openedMovies.collectAsState().value

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(text = "Просмотрено", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Text(
                    text = "${watchedMovies.size} >",
                    style = MaterialTheme.typography.h6.copy(color = MaterialTheme.colors.primary),
                    modifier = Modifier.clickable {
                            navController.navigate("watchedMovies")
                        }
                 )
            }
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                items(watchedMovies) { movie ->
                        MovieCard(item = movie, navController = navController)
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(text = "Коллекции", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }
                item {
                     Spacer(modifier = Modifier.height(10.dp))
        }
        item {
              Text(text = "+  Создать свою коллекцию",
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Medium,
                  color = Color.Blue,
                  modifier = Modifier.clickable { navController.navigate("createCollection") }
                      .padding(20.dp)
                  )
        }
                item {
                       Spacer(modifier = Modifier.height(20.dp))
            }
        item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                     horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MovieCategoryBox(
                icon = R.drawable.heart_favorite_save,
                text = "Любимые"
            ){
                navController.navigate("likedMovies")
            }
            MovieCategoryBox(
                icon = R.drawable.favorite_flag_saved,
                text = "Хочу посмотреть"
            ){
                navController.navigate("savedMovies")
            }
        }
    }
    item {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Вам было интересно", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Text(
                text = "${openedMovies.size} >",
                style = MaterialTheme.typography.h6.copy(color = MaterialTheme.colors.primary),
                modifier = Modifier
                    .clickable {
                navController.navigate("openedMovies")
                 }
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            items(openedMovies) { movie ->
                MovieCard(item = movie, navController = navController)
            }
        }
    }
    }
}

    @Composable
    fun MovieCategoryBox(
        icon: Int,
        text: String,
        onClick: () -> Unit
    ){
    Box(
            modifier = Modifier
            .height(150.dp)
            .width(150.dp)
            .clickable { onClick() }
            .padding(14.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp)),
             contentAlignment = Alignment.Center
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(25.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun MovieGridScreen(
    movies: List<Data2>,
    navController: NavController
){
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
                .padding(20.dp)
        ){
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", modifier = Modifier.size(20.dp))
            }
            Text("Movies", fontSize = 16.sp , fontWeight = FontWeight.Medium)
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ){
            items(movies) { movie ->
                MovieCard(item = movie, navController = navController)
            }
        }
    }
}




