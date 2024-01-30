package com.example.newsapplication.domain.repositories

import android.util.Log
import com.example.newsapplication.domain.models.Article
import com.example.newsapplication.domain.models.Category
import com.example.newsapplication.domain.models.SearchQuery
import com.example.newsapplication.domain.models.Source
import com.example.newsapplication.utils.DateConverter
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class NewsCachingRepository {
    private var realm: Realm =
        Realm.open(RealmConfiguration.create(schema = setOf(Article::class, Source::class)))
    private var recentArticle: Int? = null
    private var currentFetch: Int? = null
    private var currentSearchQuery: SearchQuery =
        SearchQuery(category = Category.GENERAL, query = "")
    private var _shouldUseDb: Boolean = false
    private var _maxLocalCaching: Boolean = false
    private var _categoryArticleList: RealmResults<Article>? = null

    init {
        _categoryArticleList = realm.query(Article::class, "category == $0", Category.GENERAL.value).sort("epochTime", Sort.DESCENDING)
            .find()
    }

    val maxLocalCaching
        get() = _maxLocalCaching

    fun checkUseDb(searchQuery: SearchQuery): Boolean {
        if (currentSearchQuery.category != searchQuery.category || searchQuery.query != currentSearchQuery.query) {
            currentSearchQuery.category = searchQuery.category
            currentSearchQuery.query = searchQuery.query
            currentFetch = 0
            _shouldUseDb = false
            _maxLocalCaching = false
        }
        return _shouldUseDb
    }

    suspend fun isArticleExist(queryList: List<Article>?, searchQuery: SearchQuery) {
        val category = searchQuery.category.value
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
                epochTime = DateConverter.stringToEpoch(article.publishedAt)
            }
            if (_categoryArticleList!!.firstOrNull { it.id == genId } == null) {
                writeArticle(article)
            } else {
                getDbData()
                recentArticle = i
                _shouldUseDb = true
                return
            }
        }
    }

    suspend fun setOffline() {
        getDbData()
        _shouldUseDb = true
    }

    private suspend fun getDbData() {
        runBlocking {
            _categoryArticleList = withContext(Dispatchers.IO) {
                realm.query(Article::class, "category == $0", currentSearchQuery.category.value).sort("epochTime", Sort.DESCENDING)
                    .find()
            }
        }
    }

    fun getFromDb(): List<Article> {
        if (currentFetch == null) {
            currentFetch = 10 - (recentArticle ?: 10)
        }
        val result = _categoryArticleList!!.subList(currentFetch!!, currentFetch!! + 10)
        currentFetch = currentFetch!! + 10
        if (currentFetch!! + 10 > _categoryArticleList!!.size) {
            _shouldUseDb = false
            _maxLocalCaching = true
        }
        return result
    }

    private suspend fun writeArticle(article: Article) {
        withContext(Dispatchers.IO) {
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
}