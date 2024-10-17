package com.example.kinopoisk.presentation.welcome

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.content.MediaType.Companion.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.Ro
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kinopoisk.data.WelcomeData

@Composable
fun WelcomePage(welcomePageViewModule: WelcomePageViewModule){
val pages = welcomePageViewModule.pages
    val pagerState = rememberPagerState {3}
  Column(modifier = Modifier.fillMaxSize()) {
         Row (modifier = Modifier
             .fillMaxWidth()
             .padding(horizontal = 26.dp)
             .padding(top = 80.dp),
              horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
        Text(text = "SkillCinema", fontSize = 20.sp)
        Text(text = "Пропустить", fontSize = 14.sp, modifier = Modifier.clickable {

        })
         }
      Spacer(modifier = Modifier.height(150.dp))

  }
}

@Composable
fun PagerScreen(page: WelcomeData) {
Column (
    modifier = Modifier.fillMaxWidth()
){
//Image(modifier = Modifier.fillMaxWidth(), painter = , contentDescription = )
}

}