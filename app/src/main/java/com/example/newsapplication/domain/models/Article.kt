package com.example.newsapplication.domain.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class Article : RealmObject {
    @PrimaryKey
    var id: String = ""
    var category: String = Category.GENERAL.value
    var author: String? = null
    var content: String? = null
    var description: String? = null
    var publishedAt: String? = null
    var epochTime: Long? = null
    var source: Source? = null
    var title: String? = null
    var url: String? = null
    var urlToImage: String? = null
}