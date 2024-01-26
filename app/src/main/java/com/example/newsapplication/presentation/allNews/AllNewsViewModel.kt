package com.example.newsapplication.presentation.allNews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.newsapplication.domain.models.Article
import com.example.newsapplication.domain.models.Category
import com.example.newsapplication.domain.models.SearchQuery
import com.example.newsapplication.domain.repositories.NewsCachingRepository
import com.example.newsapplication.domain.repositories.NewsRepository
import com.example.newsapplication.domain.usecase.SelectArticleUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AllNewsViewModel(
    private val newsRepository: NewsRepository,
    private val selectArticleUseCase: SelectArticleUseCase,
    private val newsCachingRepository: NewsCachingRepository
) : ViewModel() {
    private val _searchQuery: MutableLiveData<SearchQuery> = MutableLiveData(SearchQuery())
    private val _searchText: MutableLiveData<String> = MutableLiveData("")
    private val _categoryChip: MutableLiveData<Category> = MutableLiveData(Category.GENERAL)
    private var _items: MutableLiveData<Flow<PagingData<Article>>> = MutableLiveData(flow {  })

    init {
        getNews()
    }

    val items: LiveData<Flow<PagingData<Article>>>
        get() = _items
    val searchText: LiveData<String>
        get() = _searchText
    val categoryChip: LiveData<Category>
        get() = _categoryChip
    val selectedArticle: LiveData<Article>
        get() = MutableLiveData(selectArticleUseCase.selectedArticle)

    fun updateSearch(value: String) {
        _searchText.value = value
        _searchQuery.value = SearchQuery(query = value)
        getNews()
    }

    fun selectNewArticle(value: Article) {
        selectArticleUseCase.setSelectedArticle(value)
    }

    fun updateCategory(value: Category) {
        _categoryChip.value = value
        _searchQuery.value = SearchQuery(query = _searchText.value!!, category = value)
        getNews()
    }

    private fun getNews() {
        _items.value = Pager(
            pagingSourceFactory = {
                AllNewsPagingDataSource(
                    newsRepository,
                    newsCachingRepository,
                    _searchQuery.value!!
                )
            },
            config = PagingConfig(pageSize = 10)
        ).flow.cachedIn(viewModelScope)
    }
}
