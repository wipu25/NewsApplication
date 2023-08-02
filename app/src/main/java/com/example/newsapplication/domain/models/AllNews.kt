package com.example.newsapplication.domain.models

data class AllNews(
    val status: String,
    val totalResult: Int,
    val articles: ArrayList<Article>
)