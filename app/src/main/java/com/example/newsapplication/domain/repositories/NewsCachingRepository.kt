package com.example.newsapplication.domain.repositories

import android.util.Log
import com.example.newsapplication.domain.models.Article
import com.example.newsapplication.domain.models.Category
import com.example.newsapplication.domain.models.SearchQuery
import com.example.newsapplication.domain.models.Source
import com.example.newsapplication.utils.DateConverter
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import kotlinx.coroutines.coroutineScope

class NewsCachingRepository {
    private var realm: Realm =
        Realm.open(RealmConfiguration.create(schema = setOf(Article::class, Source::class)))
    private var recentArticle: Int? = null
    private var currentFetch: Int? = null
    private var currentSearchQuery: SearchQuery = SearchQuery(category = Category.GENERAL, query = "")
    private var _shouldUseDb: Boolean = false
    private var _maxLocalCaching: Boolean = false

    val maxLocalCaching
        get() = _maxLocalCaching

    fun checkUseDb(searchQuery: SearchQuery): Boolean {
        Log.d("category", searchQuery.category.value)
        if (currentSearchQuery.category != searchQuery.category || searchQuery.query != currentSearchQuery.query ) {
            currentSearchQuery.category = searchQuery.category
            currentSearchQuery.query = searchQuery.query
            currentFetch = 0
            _shouldUseDb = false
        }
        return _shouldUseDb
    }

    suspend fun isArticleExist(queryList: List<Article>?, searchQuery: SearchQuery) {
        val category = searchQuery.category.value
        val categoryArticleList =
            realm.query(Article::class, "category == $0", category)
                .find().sortedByDescending { DateConverter.stringToTime(it.publishedAt!!) }
        if (queryList.isNullOrEmpty()) {
            return
        }
        for (i in queryList.indices) {
            val article = queryList[i]
            val genId = "${article.publishedAt}+${
                article.author?.lowercase()?.trim(' ') ?: "anonymousAuthor"
            }+${category}"
            article.apply {
                id = genId
                this.category = category
            }
            if (categoryArticleList.firstOrNull { it.id == genId } == null) {
                writeArticle(article)
            } else {
                recentArticle = i
                _shouldUseDb = true
                return
            }
        }
    }

    fun setOffline() {
        _shouldUseDb = true
    }

    fun getFromDb(category: Category? = Category.GENERAL): List<Article> {
        if (currentFetch == null) {
            currentFetch = 10 - (recentArticle ?: 10)
        }
        val categoryArticleList =
            realm.query(Article::class, "category == $0", category?.value)
                .find().sortedByDescending { DateConverter.stringToTime(it.publishedAt!!) }
        val result = categoryArticleList.subList(currentFetch!!, currentFetch!! + 10)
        currentFetch = currentFetch!! + 10
        if (currentFetch!! + 10 > categoryArticleList.size) {
            _shouldUseDb = false
            _maxLocalCaching = true
        }
        return result
    }

    private suspend fun writeArticle(article: Article) {
            realm.write {
                val savedSource: Source? =
                    this.query(Source::class, "id == $0", article.source!!.id)
                        .find().firstOrNull()
                copyToRealm(article.apply {
                    if (savedSource != null) {
                        source = savedSource
                    }
                })
            }
    }
}