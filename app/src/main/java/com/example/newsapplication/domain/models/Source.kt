package com.example.newsapplication.domain.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey


class Source: RealmObject {
    @PrimaryKey
    var id: String? = null
    var name: String? = null
}