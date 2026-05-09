package com.tilda.feature.news.data.remote

import com.tilda.feature.news.data.dto.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

internal interface NewsApiService {
    @GET("news/v1/article/list")
    suspend fun getNews(
        @Query("lang") language: String,
        @Query("limit") limit: Int,
        @Query("to_ts") toTs: Long?
    ): Response<NewsResponse>
}
