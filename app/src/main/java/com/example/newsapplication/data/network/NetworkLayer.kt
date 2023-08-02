package com.example.newsapplication.data.network

import com.example.newsapplication.domain.models.AllNews
import com.example.newsapplication.domain.models.Category
import com.google.gson.GsonBuilder
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private const val API_KEY = "eb1e1e4437a94dae8bf7dc6776421ca8"

interface APIInterface {
    @GET("top-headlines?apiKey=$API_KEY")
    suspend fun getNewsByPage(
        @Query("page") page: Int,
        @Query("q") query: String? = null,
        @Query("category") category: String? = Category.GENERAL.value,
        @Query("pageSize") pageSize: Int = 10
    ): Response<AllNews>
}