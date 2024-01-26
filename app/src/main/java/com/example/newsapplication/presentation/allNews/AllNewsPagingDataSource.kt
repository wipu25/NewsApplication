package com.example.newsapplication.presentation.allNews

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.newsapplication.domain.models.Article
import com.example.newsapplication.domain.models.SearchQuery
import com.example.newsapplication.domain.repositories.NewsCachingRepository
import com.example.newsapplication.domain.repositories.NewsRepository

class AllNewsPagingDataSource(
    private val service: NewsRepository,
    private val newsCachingRepository: NewsCachingRepository,
    private val searchQuery: SearchQuery
) :
    PagingSource<Int, Article>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val pageNumber = params.key ?: 1
        var nextPageNumber: Int? = pageNumber + 1
        var data: List<Article>? = null
        if (newsCachingRepository.checkUseDb(searchQuery) && searchQuery.query.isEmpty()) {
            data = newsCachingRepository.getFromDb(searchQuery.category)
        } else {
            if (pageNumber >= 10) {
                nextPageNumber = null
            } else {
                try {
                    val response = service.getNews(pageNumber, searchQuery)
                    data = response?.articles
                    newsCachingRepository.isArticleExist(data, searchQuery)
                } catch (e: Exception) {
                    if (newsCachingRepository.maxLocalCaching) {
                        nextPageNumber = null
                    } else {
                        newsCachingRepository.setOffline()
                        data = newsCachingRepository.getFromDb(searchQuery.category)
                    }
                }
            }
        }

        if (data == null) {
            return LoadResult.Error(Throwable("Exceed request or no internet connection"))
        }

        return LoadResult.Page(
            data = data,
            prevKey = null,
            nextKey = nextPageNumber
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition
    }
}