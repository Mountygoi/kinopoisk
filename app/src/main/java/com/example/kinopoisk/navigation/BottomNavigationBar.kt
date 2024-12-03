package com.example.kinopoisk.navigation


import MoviesScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kinopoisk.data.NavigationItem
import com.example.kinopoisk.data.apiService
import domain.ActorFilmographyViewModel
import domain.ActorFilmographyViewModelFactory
import domain.ActorViewModel
import domain.FilmDetailsViewModel
import domain.FilmDetailsViewModelFactory
import domain.MoviesViewM
import domain.SharedViewModel
import presentation.ActorFilmographyPage
import presentation.ActorPagee
import presentation.FilmScreen
import presentation.GalleryDetailScreen
import presentation.MainPage
import presentation.MovieGridScreen
import presentation.ProfileScreen

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Search,
        NavigationItem.Profile
    )
    NavigationBar(containerColor = Color.White) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(text = item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun SetupNavigation(navController: NavHostController) {
    val viewM = remember { MoviesViewM(apiService) }
    val sharedViewModel: SharedViewModel = viewModel()

    NavHost(navController, startDestination = NavigationItem.Home.route) {
        composable(NavigationItem.Home.route) {
            MainPage(navController, viewM)
        }

        composable(NavigationItem.Search.route) {
        }

        composable(NavigationItem.Profile.route) {
            ProfileScreen(
                navController = navController,
                viewModel = sharedViewModel
            )
        }
        composable("watchedMovies") {
            val watchedMovies = sharedViewModel.watchedMovies.collectAsState(initial = emptyList()).value
            MovieGridScreen(movies = watchedMovies, navController = navController)
        }
        composable("likedMovies") {
            val likedMovies = sharedViewModel.likedMovies.collectAsState(initial = emptyList()).value
            MovieGridScreen(movies = likedMovies, navController = navController)
        }

        composable("savedMovies") {
            val savedMovies = sharedViewModel.savedMovies.collectAsState(initial = emptyList()).value
            MovieGridScreen(movies = savedMovies, navController = navController)
        }
        composable("openedMovies") {
            val openedMovies = sharedViewModel.openedMovies.collectAsState(initial = emptyList()).value
            MovieGridScreen(movies = openedMovies, navController = navController)
        }

        composable("details/{category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category")
            if (category != null) {
                MoviesScreen(apiService, category)
            }
        }

        composable(
            route = "movieDetails/{kinopoiskId}",
            arguments = listOf(navArgument("kinopoiskId") { type = NavType.IntType })
        ) { backStackEntry ->
            val kinopoiskId = backStackEntry.arguments?.getInt("kinopoiskId")
            if (kinopoiskId != null) {
                val filmDetailsViewModel = viewModel<FilmDetailsViewModel>(
                    factory = FilmDetailsViewModelFactory(apiService)
                )
                FilmScreen(
                    filmDetailsViewModel = filmDetailsViewModel,
                    filmId = kinopoiskId,
                    navController = navController,
                    sharedViewModel = sharedViewModel
                )
            } else {
                Text("Invalid Film ID")
            }
        }

        composable("actorDetails/{actorId}") { backStackEntry ->
            val actorId = backStackEntry.arguments?.getInt("actorId") ?: 0
            val viewModel = remember { ActorViewModel(apiService) }
            ActorPagee(viewModel = viewModel, actorId = actorId, navController = navController)
        }

        composable("gallery/{filmId}", arguments = listOf(navArgument("filmId") { type = NavType.IntType })) { backStackEntry ->
            val filmId = backStackEntry.arguments?.getInt("filmId")
            if (filmId != null) {
                GalleryDetailScreen(filmId = filmId, navController = navController)
            }
        }
        composable(
            route = "actorFilmography/{actorId}",
            arguments = listOf(navArgument("actorId") { type = NavType.IntType })
        ) { backStackEntry ->
            val actorId = backStackEntry.arguments?.getInt("actorId") ?: return@composable
            val viewModelFactory = ActorFilmographyViewModelFactory(apiService)
            val viewModel: ActorFilmographyViewModel = viewModel(factory = viewModelFactory)
            ActorFilmographyPage(
                actorId = actorId,
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            SetupNavigation(navController = navController)
        }
    }
}
