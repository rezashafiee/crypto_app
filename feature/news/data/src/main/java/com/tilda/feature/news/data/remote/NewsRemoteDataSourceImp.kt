package com.tilda.feature.news.data.remote

import com.tilda.core.data.network.safeRetrofitCall
import com.tilda.core.domain.util.NetworkError
import com.tilda.core.domain.util.Result
import com.tilda.feature.news.data.datasource.NewsRemoteDataSource
import com.tilda.feature.news.data.mapper.toNewsArticle
import com.tilda.feature.news.domain.model.NewsArticle
import javax.inject.Inject

class NewsRemoteDataSourceImp @Inject constructor(
    private val newsApiService: NewsApiService
) : NewsRemoteDataSource {

    private companion object {
        const val NEWS_LANGUAGE = "EN"
    }

    override suspend fun getNews(
        limit: Int,
        toTs: Long?
    ): Result<List<NewsArticle>, NetworkError> {
        return safeRetrofitCall(
            execute = {
                newsApiService.getNews(
                    language = NEWS_LANGUAGE,
                    limit = limit,
                    toTs = toTs
                )
            },
            mapper = { body ->
                body.data
                    .map { it.toNewsArticle() }
                    .filter { it.title.isNotBlank() }
            }
        )
    }
}
