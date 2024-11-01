package com.example.kinopoisk.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector


sealed class NavigationItem(val route: String, val icon: ImageVector, val title : String) {
    data object Home : NavigationItem("home", Icons.Filled.Home, "Home")
    data object Search : NavigationItem("search", Icons.Filled.Search, "Search")
    data object Profile : NavigationItem("profile", Icons.Filled.Person, "Profile")
}
