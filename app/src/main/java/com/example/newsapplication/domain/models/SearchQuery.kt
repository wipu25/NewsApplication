package com.example.newsapplication.domain.models

enum class Category(val value: String) {
    BUSINESS("business"),
    ENTERTAINMENT("entertainment"),
    GENERAL("general"),
    HEALTH("health"),
    SCIENCE("science"),
    SPORTS("sports"),
    TECHNOLOGY("technology")
}

data class SearchQuery(
    val query: String = "",
    val category: Category = Category.GENERAL
)