package com.example.newsone.models

import com.google.gson.annotations.SerializedName

data class NewsArticle(
    val title: String,
    val description: String?,
    val urlToImage: String?,
    val url: String,
    val content: String?,         // Add content field
    val author: String?,           // Add author field
    val source: Source,
    @SerializedName("publishedAt") val publishedAt: String? // Date and time the article was published
)

data class Source(
    val id: String?,
    val name: String
)

data class NewsResponse(
    val articles: List<NewsArticle>
)
