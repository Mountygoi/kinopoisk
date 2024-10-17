package com.example.kinopoisk.presentation.welcome

import androidx.lifecycle.ViewModel
import com.example.kinopoisk.R
import com.example.kinopoisk.data.WelcomeData

class WelcomePageViewModule : ViewModel(){
  val pages = listOf(
      WelcomeData(R.drawable.first , "Узнавай \nо премьерах"),
      WelcomeData(R.drawable.second , "Создавай \nколлекции"),
      WelcomeData(R.drawable.third , "Делись \nс друзьями")
  )
}