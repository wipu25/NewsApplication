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
    var query: String = "",
    var category: Category = Category.GENERAL
)