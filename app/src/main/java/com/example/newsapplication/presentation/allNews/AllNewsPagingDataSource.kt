package com.example.newsapplication.presentation.allNews

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.newsapplication.domain.models.Article
import com.example.newsapplication.domain.models.SearchQuery
import com.example.newsapplication.domain.repositories.NetworkRepository

class AllNewsPagingDataSource(
    private val service: NetworkRepository,
    private val searchQuery: SearchQuery? = null
) :
    PagingSource<Int, Article>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val pageNumber = params.key ?: 1
        return try {
            val response = service.getNews(pageNumber, searchQuery)
            val data = response?.articles
            val nextPageNumber = pageNumber + 1

            LoadResult.Page(
                data = data.orEmpty(),
                prevKey = null,
                nextKey = nextPageNumber
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition
    }
}