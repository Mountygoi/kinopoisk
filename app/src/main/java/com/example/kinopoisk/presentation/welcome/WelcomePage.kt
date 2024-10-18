package com.example.kinopoisk.presentation.welcome

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kinopoisk.data.WelcomeData

@Composable
fun WelcomePage(navController: NavController,
    welcomePageViewModule: WelcomePageViewModule = viewModel()) {
    val pages = welcomePageViewModule.pages
    val pagerState = rememberPagerState { 3 }
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 26.dp)
                .padding(top = 80.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "SkillCinema", fontSize = 20.sp)
            Text(text = "Пропустить", fontSize = 14.sp, modifier = Modifier.clickable {
              navController.navigate("home")
            })
        }
        Spacer(modifier = Modifier.height(150.dp))
Column(modifier = Modifier.fillMaxWidth()) {
    HorizontalPager(state = pagerState) { page ->
        PagerScreen(page = pages[page])
    }

    Spacer(modifier = Modifier.height(56.dp))
    Dots(
            total = pages.size,
        selected = pagerState.currentPage,
                modifier = Modifier.padding(start = 20.dp)
        )

}
    }
}

@Composable
fun PagerScreen(page: WelcomeData) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(270.dp), painter = painterResource(id = page.image), contentDescription = ""
        )
        Spacer(modifier = Modifier.height(67.dp))
        Text(
            modifier = Modifier
                .width(250.dp)
                .height(70.dp)
                .padding(start = 20.dp), style = TextStyle(lineHeight = 35.2.sp),
            text = page.title, fontSize = 32.sp
        )
    }

}

@Composable
fun Dots(total: Int, selected: Int ,modifier: Modifier=Modifier) {
    Row(
        modifier = modifier
            .width(32.dp)
            .height(8.dp), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        repeat(total) { index ->
            Box(modifier = Modifier
                .size(8.dp)
                .background(
                    color = if (index == selected) Color.Black else Color.Gray,
                    shape = CircleShape
                )
            )
        }
    }

}