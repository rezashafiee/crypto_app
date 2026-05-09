package com.tilda.feature.news.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.tilda.feature.news.data.local.NewsEntity
import com.tilda.feature.news.data.mapper.toNewsArticle
import com.tilda.feature.news.domain.model.NewsArticle
import com.tilda.feature.news.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsRepositoryImp @Inject constructor(
    private val pager: Pager<Int, NewsEntity>
) : NewsRepository {
    override fun getPagedNews(): Flow<PagingData<NewsArticle>> {
        return pager.flow.map { pagingData ->
            pagingData.map { newsEntity -> newsEntity.toNewsArticle() }
        }
    }
}
