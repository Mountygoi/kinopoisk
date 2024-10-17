package com.example.kinopoisk.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kinopoisk.presentation.welcome.WelcomePage

@Composable
fun NavigationToHomePage() {
val navigation = rememberNavController()
    NavHost(navController = navigation, startDestination = "welcome", builder = {
        composable("welcome"){
            WelcomePage(navigation)
        }
//        composable("home"){
//              MainScreen()
//        }



    })
    
}