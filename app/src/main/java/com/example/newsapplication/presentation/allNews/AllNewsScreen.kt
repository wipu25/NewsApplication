package com.example.newsapplication.presentation.allNews

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.newsapplication.R
import com.example.newsapplication.di.allNewsModule
import com.example.newsapplication.domain.models.Category
import com.example.newsapplication.presentation.allNews.components.CategoryChip
import com.example.newsapplication.presentation.allNews.components.CategoryRow
import com.example.newsapplication.presentation.allNews.components.NewsItem
import com.example.newsapplication.presentation.allNews.components.NewsList
import com.example.newsapplication.presentation.allNews.components.SearchRow
import com.example.newsapplication.presentation.ui.theme.Shapes
import org.koin.androidx.compose.getViewModel
import org.koin.compose.module.rememberKoinModules
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun AllNewsScreen(navigateToArticle: () -> Unit) {
    rememberKoinModules(unloadOnForgotten = true, modules = { listOf(allNewsModule) })
    Box {
        Column {
            SearchRow()
            CategoryRow()
            NewsList(navigateToArticle = navigateToArticle)
        }
    }
}