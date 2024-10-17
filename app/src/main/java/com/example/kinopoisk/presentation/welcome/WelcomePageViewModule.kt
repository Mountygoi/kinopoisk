package com.example.kinopoisk.presentation.welcome

import com.example.kinopoisk.R
import com.example.kinopoisk.data.WelcomeData

class WelcomePageViewModule {
  val pages = listOf(
      WelcomeData(R.drawable.first , "Узнавай \nо премьерах"),
      WelcomeData(R.drawable.second , "Создавай \nколлекции"),
      WelcomeData(R.drawable.third , "Делись \nс друзьями")
  )
}