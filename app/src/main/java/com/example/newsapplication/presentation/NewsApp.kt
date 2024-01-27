package com.example.newsapplication.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.newsapplication.R
import com.example.newsapplication.presentation.allNews.AllNewsScreen
import com.example.newsapplication.presentation.newsArticle.NewsArticleScreen

enum class NewsScreen(@StringRes val title: Int) {
    AllNews(title = R.string.all_news),
    NewsArticle(title = R.string.news_article),
}

@Composable
fun NewsAppBar(
    navController: NavHostController,
) {
    val currentScreen = NewsScreen.valueOf(
        navController.currentBackStackEntryAsState().value?.destination?.route
            ?: NewsScreen.AllNews.name
    )
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        navigationIcon = {
            if (navController.previousBackStackEntry != null) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun NewsApp(
    navController: NavHostController = rememberNavController(),
) {
    Scaffold(
        topBar = {
            NewsAppBar(
                navController = navController
            )
        },
        backgroundColor = Color.LightGray
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NewsScreen.AllNews.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NewsScreen.AllNews.name) {

                AllNewsScreen(
                    navigateToArticle = { navController.navigate(NewsScreen.NewsArticle.name) })
            }
            composable(NewsScreen.NewsArticle.name) {
                NewsArticleScreen()
            }
        }
    }
}