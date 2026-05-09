package com.tilda.feature.news.domain.model

import java.time.ZonedDateTime

data class NewsArticle(
    val id: String,
    val title: String,
    val body: String,
    val authors: String,
    val source: String,
    val url: String,
    val imageUrl: String,
    val publishedOn: ZonedDateTime,
    val categories: List<String>
)
