package com.example.newsapplication.presentation.allNews

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.newsapplication.domain.models.Article
import com.example.newsapplication.domain.models.SearchQuery
import com.example.newsapplication.domain.repositories.NewsCachingRepository
import com.example.newsapplication.domain.repositories.NewsRepository

class AllNewsPagingDataSource(
    private val service: NewsRepository,
    private val newsCachingRepository: NewsCachingRepository,
    private val searchQuery: SearchQuery? = null
) :
    PagingSource<Int, Article>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val pageNumber = params.key ?: 1
        var nextPageNumber: Int? = pageNumber + 1
        var data: List<Article>? = listOf()
//        return try {
        if(newsCachingRepository.shouldUseDb) {
            data = newsCachingRepository.getFromDb()
        } else {
            if(pageNumber >= 10) {
                nextPageNumber = null
            } else {
                val response = service.getNews(pageNumber, searchQuery)
                data = response?.articles
                newsCachingRepository.isArticleExist(data)
            }
        }

        return    LoadResult.Page(
                data = data.orEmpty(),
                prevKey = null,
                nextKey = nextPageNumber
            )
//        } catch (e: Exception) {
//            Log.e("error on loading", e.message.toString())
//            LoadResult.Error(e)
//        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition
    }
}