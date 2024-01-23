package com.example.newsapplication.domain.repositories

import android.util.Log
import androidx.compose.ui.text.toLowerCase
import com.example.newsapplication.domain.models.Article
import com.example.newsapplication.domain.models.Source
import com.example.newsapplication.utils.DateConverter
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope

class NewsCachingRepository {
    private var realm: Realm = Realm.open(RealmConfiguration.create(schema = setOf(Article::class, Source::class)))
    private var articleList: List<Article> = realm.query(Article::class).find().sortedByDescending { DateConverter.stringToTime(it.publishedAt!!) }
    private var recentArticle: Int? = null
    private var currentFetch: Int? = null
    private var _shouldUseDb: Boolean = false

    val shouldUseDb : Boolean
        get() = _shouldUseDb
    suspend fun isArticleExist(queryList: ArrayList<Article>?) {
        if(queryList.isNullOrEmpty()) {
            return
        }
        for(i in queryList.indices) {
            val article = queryList[i]
            val genId = "${article.publishedAt}+${article.author?.lowercase()?.trim(' ') ?: "anonymousAuthor"}"
            article.apply{
                id = genId
            }
            if(articleList.firstOrNull{ it.id == genId } == null) {
                writeArticle(article)
            } else {
                recentArticle = i
                _shouldUseDb = true
                return
            }
        }
    }

    fun getFromDb(): List<Article> {
//        Log.d("current fetch", currentFetch.toString())
        if(currentFetch == null) {
            currentFetch = 10 - recentArticle!!
        }
        val result = articleList.subList(currentFetch!!, currentFetch!! + 10)
        currentFetch = currentFetch!! + 10
        if(currentFetch!! + 10 > articleList.size) {
            _shouldUseDb = false
        }
        return result
    }

    private suspend fun writeArticle(article: Article) {
        coroutineScope {
            realm.write {
                val savedSource: Source? = this.query(Source::class, "id == $0",article.source!!.id).find().firstOrNull()
                copyToRealm(article.apply {
                    if(savedSource != null) {
                        source = savedSource
                    }
                })
            }
        }
    }
}