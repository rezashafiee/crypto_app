package com.tilda.feature.news.data.datasource

import com.tilda.core.domain.util.NetworkError
import com.tilda.core.domain.util.Result
import com.tilda.feature.news.domain.model.NewsArticle

internal interface NewsRemoteDataSource {
    suspend fun getNews(
        limit: Int,
        toTs: Long? = null
    ): Result<List<NewsArticle>, NetworkError>
}
