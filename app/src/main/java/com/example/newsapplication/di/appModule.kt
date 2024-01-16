package com.example.newsapplication.di

import com.example.newsapplication.data.network.APIInterface
import com.example.newsapplication.domain.models.Item
import com.example.newsapplication.domain.repositories.NetworkRepository
import com.example.newsapplication.presentation.allNews.AllNewsViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private const val baseUrl: String = "https://newsapi.org/v2/"

val appModule = module {
    single<Gson> {
        GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create()
    }
    single<Retrofit> {
        Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(get<Gson>()))
            .build()
    }
    single { get<Retrofit>().create(APIInterface::class.java) }
    single { NetworkRepository(get()) }
    single { Realm.open(RealmConfiguration.create(schema = setOf(Item::class))) }
    viewModel { AllNewsViewModel(get()) }
}