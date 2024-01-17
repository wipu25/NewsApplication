package com.example.newsapplication.domain.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.*

class Article : RealmObject {
    @PrimaryKey
    var id: String = ""
    var author: String? = null
    var content: String? = null
    var description: String? = null
    var publishedAt: String? = null
    var source: Source? = null
    var title: String? = null
    var url: String? = null
    var urlToImage: String? = null
}