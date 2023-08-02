package com.example.newsapplication.domain.models

import java.util.*

data class Article(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: Date?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
)