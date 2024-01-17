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
import com.example.newsapplication.domain.repositories.NewsRepository
import com.example.newsapplication.domain.usecase.SelectArticleUseCase
import kotlinx.coroutines.flow.Flow

class AllNewsViewModel(private val newsRepository: NewsRepository, private val selectArticleUseCase: SelectArticleUseCase) : ViewModel() {
    private val _searchQuery: MutableLiveData<SearchQuery?> = MutableLiveData(SearchQuery())
    private val _searchText: MutableLiveData<String> = MutableLiveData("")
    private val _categoryChip: MutableLiveData<Category> = MutableLiveData(Category.GENERAL)

    val searchText: LiveData<String>
        get() = _searchText
    val categoryChip: LiveData<Category>
        get() = _categoryChip
    val selectedArticle: LiveData<Article>
        get() = MutableLiveData(selectArticleUseCase.selectedArticle)

    fun updateSearch(value: String) {
        _searchText.value = value
        _searchQuery.value = SearchQuery(query = value)
    }

    fun selectNewArticle(value: Article) {
        selectArticleUseCase.setSelectedArticle(value)
    }

    fun updateCategory(value: Category) {
        _categoryChip.value = value
        _searchQuery.value = SearchQuery(query = _searchText.value!!, category = value)
    }

    fun getNews(): Flow<PagingData<Article>> {
        return Pager(
            pagingSourceFactory = {
                AllNewsPagingDataSource(
                    newsRepository,
                    _searchQuery.value
                )
            },
            config = PagingConfig(pageSize = 10)
        ).flow.cachedIn(viewModelScope)
    }
}
