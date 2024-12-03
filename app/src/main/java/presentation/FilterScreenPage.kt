package presentation




import MoviesScreen
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.example.kinopoisk.R
import com.example.kinopoisk.data.KinoPoiskApi
import com.example.kinopoisk.data.Movie
import com.example.kinopoisk.data.NavigationItem
import com.example.kinopoisk.data.apiService
import domain.ActorFilmographyViewModel
import domain.ActorFilmographyViewModelFactory
import domain.ActorViewModel
import domain.FilmDetailsViewModel
import domain.FilmDetailsViewModelFactory
import domain.MoviesViewM
import kotlinx.coroutines.launch
import presentation.ActorFilmographyPage
import presentation.ActorPagee
import presentation.FilmScreen
import presentation.GalleryDetailScreen
import presentation.MainPage
import java.util.Calendar

@Composable
fun FilteredMoviesScreen(
    apiService: KinoPoiskApi,
    filters: FilterParams
) {
    var movies by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }


    LaunchedEffect(filters) {
        isLoading = true
        try {
            // Более надежная обработка фильтров
            val response = apiService.getMovies(
                order = when (filters.sortOption) {
                    "Дата" -> "YEAR"
                    "Популярность" -> "NUM_VOTE"
                    "Рейтинг" -> "RATING"
                    else -> "NUM_VOTE"
                },
                type = when (filters.type) {
                    "Все" -> "ALL"
                    "Фильмы" -> "FILM"
                    "Сериалы" -> "TV_SERIES"
                    else -> "ALL"
                },
                ratingFrom = filters.ratingRange.start.toInt(),
                ratingTo = filters.ratingRange.endInclusive.toInt(),
                yearFrom = filters.yearRange?.start?:0,
                yearTo = filters.yearRange?.endInclusive?:0,
                genre = filters.genres.firstOrNull()?.trim()?.takeIf { it.isNotEmpty() },
                country = filters.countries.firstOrNull()?.trim()?.takeIf { it.isNotEmpty() },
                page = 1
            )


            if (response.isSuccessful) {
                movies = response.body()?.items?.filter { movie ->
                    val genreMatch = filters.genres.isEmpty() ||
                            filters.genres.any { selectedGenre ->
                                movie.genres?.any {
                                    it.genre.contains(selectedGenre, ignoreCase = true)
                                } ?: false
                            }


                    val countryMatch = filters.countries.isEmpty() ||
                            filters.countries.any { selectedCountry ->
                                movie.countries?.any {
                                    it.country.contains(selectedCountry, ignoreCase = true)
                                } ?: false
                            }


                    genreMatch && countryMatch
                } ?: emptyList()
            } else {
                errorMessage = "Ошибка: ${response.code()}"
            }
        } catch (e: Exception) {
            errorMessage = "Ошибка сети: ${e.message}"
        } finally {
            isLoading = false
        }
    }


    if (isLoading) {
        CircularProgressIndicator()
    } else if (errorMessage.isNotEmpty()) {
        Text("Ошибка: $errorMessage")
    } else if (movies.isEmpty()) {
        Column {
            Text("Фильмов по заданным фильтрам не найдено")
        }
    }
    else {
        MovieList(movies)
    }
}
@Composable
fun MovieList(movies: List<Movie>) {
    LazyColumn {
        items(movies) { movie ->
            MovieCardd(movie)
        }
    }
}




@Composable
fun FilterScreen(navController: NavController, onApplyFilters: (FilterParams) -> Unit) {
    val filtersViewModel: FiltersViewModel =
        viewModel(factory = FiltersViewModelFactory(apiService))
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val startYear = savedStateHandle?.get<Int>("startYear")
    val endYear = savedStateHandle?.get<Int>("endYear")
    var selectedType by remember { mutableStateOf("Все") }
    var selectedCountry by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf("") }
    var selectedRatingRange by remember { mutableStateOf(1f..10f) }
    var selectedSortOption by remember { mutableStateOf("Дата") }
    var onlyUnwatched by remember { mutableStateOf(false) }
    navController.currentBackStackEntry?.savedStateHandle?.getLiveData<String>("selectedCountry")?.value?.let { country ->
        selectedCountry = country
    }
    navController.currentBackStackEntry?.savedStateHandle?.getLiveData<String>("selectedGenre")?.value?.let { genre ->
        selectedGenre = genre
    }
    LaunchedEffect(Unit) {
        filtersViewModel.loadFilters()


    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(40.dp))


        Text("Показывать",modifier = Modifier.padding(horizontal = 16.dp), fontSize = 15.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            FilterOptionButton("Все", selectedType) { selectedType = it }
            FilterOptionButton("Фильмы", selectedType) { selectedType = it }
            FilterOptionButton("Сериалы", selectedType) { selectedType = it }
        }
        Spacer(modifier = Modifier.height(20.dp))




        if (filtersViewModel.countries.isNotEmpty()) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clickable { navController.navigate("countryListScreen") }
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(0.dp)
                    )
                    .padding(top = 4.dp, bottom = 4.dp)  // Optional padding for spacing
            ) {
                Text("Страна", style = MaterialTheme.typography.bodyLarge, fontSize = 20.sp,modifier = Modifier.padding(horizontal = 16.dp))
                Text("$selectedCountry", style = MaterialTheme.typography.bodyLarge,modifier = Modifier.padding(horizontal = 16.dp))
            }
        }


        if (filtersViewModel.genres.isNotEmpty()) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clickable { navController.navigate("genreListScreen") }
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(0.dp)
                    )
                    .padding(top = 4.dp, bottom = 4.dp)  // Optional padding for spacing
            ){
                Text("Жанр",modifier = Modifier.padding(horizontal = 16.dp), style = MaterialTheme.typography.bodyLarge, fontSize = 20.sp)
                Text("$selectedGenre", style = MaterialTheme.typography.bodyLarge,modifier = Modifier.padding(horizontal = 16.dp))
            }
        }


        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clickable { navController.navigate("yearListScreen") }
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(0.dp)
                )
                .padding(top = 4.dp, bottom = 4.dp)
        ) {
            Text("Год",modifier = Modifier.padding(horizontal = 16.dp), style = MaterialTheme.typography.bodyLarge, fontSize = 20.sp)
            val yearText = if (startYear != null && endYear != null) {
                "$startYear - $endYear"
            } else {
                ""
            }
            Text(yearText, style = MaterialTheme.typography.bodyLarge, fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))


        Text("Рейтинг", modifier = Modifier.padding(horizontal = 16.dp))
        RangeSlider(modifier = Modifier.padding(horizontal = 16.dp),
            value = selectedRatingRange,
            valueRange = 1f..10f,
            onValueChange = { range ->
                selectedRatingRange = range.start..range.endInclusive
            }
        )
        Text("${selectedRatingRange.start.toInt()} - ${selectedRatingRange.endInclusive.toInt()}",modifier = Modifier.padding(horizontal = 16.dp))


        Spacer(modifier = Modifier.height(16.dp))


        Text("Сортировать",modifier = Modifier.padding(16.dp))
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp).padding(horizontal = 16.dp)) {
            FilterOptionButton("Дата", selectedSortOption) { selectedSortOption = it }
            FilterOptionButton("Популярность", selectedSortOption) { selectedSortOption = it }
            FilterOptionButton("Рейтинг", selectedSortOption) { selectedSortOption = it }
        }


        Spacer(modifier = Modifier.height(16.dp))


        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Не просмотрен")
            Switch(checked = onlyUnwatched, onCheckedChange = { onlyUnwatched = it })
        }


        Spacer(modifier = Modifier.height(16.dp))


        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                val yearRange = if (startYear != null && endYear != null) {
                    startYear..endYear
                } else {
                    null // Если год не выбран, оставляем null
                }
                val filters = FilterParams(
                    type = selectedType,
                    yearRange = yearRange,
                    ratingRange = selectedRatingRange,
                    sortOption = selectedSortOption,
                    onlyUnwatched = onlyUnwatched,
                    countries = if (selectedCountry.isNotBlank()) listOf(selectedCountry) else emptyList(),
                    genres = if (selectedGenre.isNotBlank()) listOf(selectedGenre) else emptyList(),
                )
                onApplyFilters(filters)
                navController.navigate("filtered_movies_screen")
            }
        ) {
            Text("Применить")
        }
    }
}


@Composable
fun FilterOptionButton(
    text: String,
    selectedOption: String,
    onClick: (String) -> Unit
) {
    Button(
        onClick = { onClick(text) },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selectedOption == text) Color.Blue else Color.Gray
        ),
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Text(text, color = Color.White)
    }
}

class FiltersViewModel(private val apiService: KinoPoiskApi) : ViewModel() {
    var genres by mutableStateOf<List<String>>(emptyList())
    var countries by mutableStateOf<List<String>>(emptyList())


    fun loadFilters() {
        viewModelScope.launch {
            try {
                val response = apiService.getFilters()
                if (response.isSuccessful) {
                    genres = response.body()?.genres?.map { it.genre } ?: emptyList()
                    countries = response.body()?.countries?.map { it.country } ?: emptyList()
                }
            } catch (e: Exception) {
            }
        }
    }
}
class FiltersViewModelFactory(private val apiService: KinoPoiskApi) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FiltersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FiltersViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
@Composable
fun CountryListScreen(navController: NavController, apiService: KinoPoiskApi) {
    var countries by remember { mutableStateOf<List<String>>(emptyList()) }
    var filteredCountries by remember { mutableStateOf<List<String>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }


    LaunchedEffect(Unit) {
        try {
            val response = apiService.getFilters()
            if (response.isSuccessful) {
                countries = response.body()?.countries?.map { it.country } ?: emptyList()
                filteredCountries = countries
            }
        } catch (e: Exception) {
        } finally {
            isLoading = false
        }
    }


    LaunchedEffect(searchQuery) {
        filteredCountries = countries.filter { it.contains(searchQuery, ignoreCase = true) }
    }

    if (isLoading) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize().wrapContentSize())
    } else {
        Column {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Введите Страну") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp) ,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search Icon",
                        tint = Color.Gray
                    )
                },colors = androidx.compose.material3.TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )


            LazyColumn {
                items(filteredCountries) { country ->
                    Text(
                        text = country,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.previousBackStackEntry?.savedStateHandle?.set(
                                    "selectedCountry",
                                    country
                                )
                                navController.popBackStack()
                            }
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}
@Composable
fun GenreListScreen(navController: NavController, apiService: KinoPoiskApi) {
    var genres by remember { mutableStateOf<List<String>>(emptyList()) }
    var filteredGenres by remember { mutableStateOf<List<String>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }


    LaunchedEffect(Unit) {
        try {
            val response = apiService.getFilters()
            if (response.isSuccessful) {
                genres = response.body()?.genres?.map { it.genre } ?: emptyList()
                filteredGenres = genres
            }
        } catch (e: Exception) {
        } finally {
            isLoading = false
        }
    }


    LaunchedEffect(searchQuery) {
        filteredGenres = genres.filter { it.contains(searchQuery, ignoreCase = true) }
    }


    if (isLoading) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize().wrapContentSize())
    } else {
        Column {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Введите Жанр") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search Icon",
                        tint = Color.Gray
                    )
                },colors = androidx.compose.material3.TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )


            LazyColumn {
                items(filteredGenres) { genre ->
                    Text(
                        text = genre,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                              navController.previousBackStackEntry?.savedStateHandle?.set(
                                    "selectedGenre",
                                    genre
                                )
                                navController.popBackStack()
                            }
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun YearRangePicker(
    modifier: Modifier = Modifier,
    startYear: Int = 1900,
    endYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    initialSelectedYear: Int,
    onYearSelected: (Int) -> Unit
) {
    var selectedYear by remember { mutableStateOf(initialSelectedYear) }
    var currentRangeStart by remember { mutableStateOf(startYear) }
    val yearsPerPage = 12
    val currentRangeEnd = (currentRangeStart + yearsPerPage - 1).coerceAtMost(endYear)


    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                if (currentRangeStart > startYear) {
                    currentRangeStart -= yearsPerPage
                }
            }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Previous")
            }
            Text(
                text = "$currentRangeStart - $currentRangeEnd",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            IconButton(onClick = {
                if (currentRangeEnd < endYear) {
                    currentRangeStart += yearsPerPage
                }
            }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Next")
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            items((currentRangeStart..currentRangeEnd).toList()) { year ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (year == selectedYear) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surface
                        )
                        .clickable {
                            selectedYear = year
                            onYearSelected(year)
                        }
                ) {
                    Text(
                        text = year.toString(),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        color = if (year == selectedYear) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
@Composable
fun YearRangePickerScreen(
    onRangeSelected: (Int, Int) -> Unit
) {
    var startYear by remember { mutableStateOf(1998) } // Год "с"
    var endYear by remember { mutableStateOf(2009) }   // Год "до"


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Искать в период с",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            YearRangePicker(
                startYear = 1900,
                endYear = Calendar.getInstance().get(Calendar.YEAR),
                initialSelectedYear = startYear,
                onYearSelected = { year -> startYear = year },
                modifier = Modifier.fillMaxWidth()
            )


            Spacer(modifier = Modifier.height(16.dp))


            Text(
                text = "Искать в период до",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            YearRangePicker(
                startYear = 1900,
                endYear = Calendar.getInstance().get(Calendar.YEAR),
                initialSelectedYear = endYear,
                onYearSelected = { year -> endYear = year },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Button(
            onClick = {
                onRangeSelected(startYear, endYear)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(text = "Выбрать")
        }
    }
}
