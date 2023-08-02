package com.example.newsapplication.domain.repositories

import com.example.newsapplication.data.network.APIInterface
import com.example.newsapplication.domain.models.AllNews
import com.example.newsapplication.domain.models.SearchQuery

class NetworkRepository(
    private val apiInterface: APIInterface
) {
    suspend fun getNews(page: Int, searchQuery: SearchQuery? = null): AllNews? {
        val result =
            apiInterface.getNewsByPage(page, searchQuery?.query, searchQuery?.category?.value, 10)
        if (result.code() != 200) {
            throw Exception("Error receiving news")
        }
        return result.body()
    }
}