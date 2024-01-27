package com.example.newsapplication.presentation.allNews

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.example.newsapplication.presentation.allNews.components.CategoryRow
import com.example.newsapplication.presentation.allNews.components.NewsList
import com.example.newsapplication.presentation.allNews.components.SearchRow

@Composable
fun AllNewsScreen(navigateToArticle: () -> Unit) {
    Box {
        Column {
            SearchRow()
            CategoryRow()
            NewsList(navigateToArticle = navigateToArticle)
        }
    }
}