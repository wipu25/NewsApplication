package com.example.newsapplication.presentation.newsArticle

import androidx.lifecycle.ViewModel
import com.example.newsapplication.domain.models.Article
import com.example.newsapplication.domain.usecase.SelectArticleUseCase

class NewsArticleViewModel(private val selectArticleUseCase: SelectArticleUseCase) : ViewModel() {
    val selectArticle: Article
        get() = selectArticleUseCase.selectedArticle
}