package com.example.newsapplication.di

import com.example.newsapplication.data.network.APIInterface
import com.example.newsapplication.domain.repositories.NewsCachingRepository
import com.example.newsapplication.domain.repositories.NewsRepository
import com.example.newsapplication.domain.usecase.SelectArticleUseCase
import com.example.newsapplication.presentation.allNews.AllNewsViewModel
import com.example.newsapplication.presentation.newsArticle.NewsArticleViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
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
    single { NewsRepository(get()) }
    single { NewsCachingRepository() }
}

val newsModule = module {
    single { SelectArticleUseCase() }
    viewModel { AllNewsViewModel(get(), get(), get()) }
    viewModel { NewsArticleViewModel(get()) }
}