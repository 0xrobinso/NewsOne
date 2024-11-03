package com.example.newsone.api

import com.example.newsone.models.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("v2/top-headlines")
    fun getTopHeadlines(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String
    ): Call<NewsResponse>

    // New search method
    @GET("v2/everything")
    fun searchNews(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String
    ): Call<NewsResponse>

    @GET("v2/top-headlines")
    fun getGeneralNews(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String = "05f57c89ed4947a9a255769ef7650b57"
    ): Call<NewsResponse>
}
