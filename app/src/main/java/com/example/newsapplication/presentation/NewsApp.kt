package com.example.newsapplication.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.newsapplication.presentation.allNews.AllNewsViewModel
import com.example.newsapplication.presentation.newsArticle.NewsArticleScreen
import org.koin.androidx.compose.koinViewModel

enum class NewsScreen(@StringRes val title: Int) {
    AllNews(title = R.string.all_news),
    NewsArticle(title = R.string.news_article),
}

@Composable
fun NewsAppBar(
    currentScreen: NewsScreen,
    canNavigateBack: Boolean,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
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
    viewModel: AllNewsViewModel = koinViewModel()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = NewsScreen.valueOf(
        backStackEntry?.destination?.route ?: NewsScreen.AllNews.name
    )
    Scaffold(
        topBar = {
            NewsAppBar(currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateBack = { navController.navigateUp() }
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
                    viewModel,
                    navigateToArticle = { navController.navigate(NewsScreen.NewsArticle.name) })
            }
            composable(NewsScreen.NewsArticle.name) {
                NewsArticleScreen(viewModel.selectedArticle.value)
            }
        }
    }
}