package com.example.newsapplication.domain.usecase

import com.example.newsapplication.domain.models.Article

class SelectArticleUseCase {
    private lateinit var _selectedArticle: Article
    val selectedArticle: Article
        get() = _selectedArticle

    fun setSelectedArticle(article: Article) {
        _selectedArticle = article
    }
}