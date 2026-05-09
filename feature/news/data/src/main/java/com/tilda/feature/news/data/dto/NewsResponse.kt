package com.tilda.feature.news.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewsResponse(
    @field:Json(name = "Data")
    val data: List<NewsArticleDto> = emptyList()
)

@JsonClass(generateAdapter = true)
data class NewsArticleDto(
    @field:Json(name = "ID")
    val id: Long? = null,
    @field:Json(name = "GUID")
    val guid: String? = null,
    @field:Json(name = "TITLE")
    val title: String? = null,
    @field:Json(name = "BODY")
    val body: String? = null,
    @field:Json(name = "AUTHORS")
    val authors: String? = null,
    @field:Json(name = "URL")
    val url: String? = null,
    @field:Json(name = "IMAGE_URL")
    val imageUrl: String? = null,
    @field:Json(name = "PUBLISHED_ON")
    val publishedOn: Long? = null,
    @field:Json(name = "SOURCE_DATA")
    val sourceData: NewsSourceDto? = null,
    @field:Json(name = "CATEGORY_DATA")
    val categoryData: List<NewsCategoryDto>? = null
)

@JsonClass(generateAdapter = true)
data class NewsSourceDto(
    @field:Json(name = "NAME")
    val name: String? = null,
    @field:Json(name = "URL")
    val url: String? = null
)

@JsonClass(generateAdapter = true)
data class NewsCategoryDto(
    @field:Json(name = "CATEGORY")
    val category: String? = null,
    @field:Json(name = "NAME")
    val name: String? = null
)
