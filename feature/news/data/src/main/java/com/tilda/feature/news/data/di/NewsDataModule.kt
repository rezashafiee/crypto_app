package com.tilda.feature.news.data.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.tilda.feature.news.data.datasource.NewsLocalDataSource
import com.tilda.feature.news.data.datasource.NewsRemoteDataSource
import com.tilda.feature.news.data.local.NewsDao
import com.tilda.feature.news.data.local.NewsDatabase
import com.tilda.feature.news.data.local.NewsEntity
import com.tilda.feature.news.data.local.NewsLocalDataSourceImp
import com.tilda.feature.news.data.paging.NewsRemoteMediator
import com.tilda.feature.news.data.remote.NewsApiService
import com.tilda.feature.news.data.remote.NewsRemoteDataSourceImp
import com.tilda.feature.news.data.repository.NewsRepositoryImp
import com.tilda.feature.news.domain.interactor.GetNewsUseCase
import com.tilda.feature.news.domain.repository.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NewsDataModule {

    @Binds
    @Singleton
    abstract fun bindNewsRemoteDataSource(
        implementation: NewsRemoteDataSourceImp
    ): NewsRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindNewsLocalDataSource(
        implementation: NewsLocalDataSourceImp
    ): NewsLocalDataSource

    @Binds
    @Singleton
    abstract fun bindNewsRepository(
        implementation: NewsRepositoryImp
    ): NewsRepository

    companion object {
        @Provides
        @Singleton
        fun provideNewsApiService(retrofit: Retrofit): NewsApiService {
            return retrofit.create(NewsApiService::class.java)
        }

        @Provides
        @Singleton
        fun provideGetNewsUseCase(repository: NewsRepository): GetNewsUseCase {
            return GetNewsUseCase(repository)
        }

        @Provides
        @Singleton
        fun provideNewsDatabase(
            @ApplicationContext context: Context
        ): NewsDatabase {
            return Room.databaseBuilder(
                context,
                NewsDatabase::class.java,
                "news_database"
            ).build()
        }

        @Provides
        @Singleton
        fun provideNewsDao(database: NewsDatabase): NewsDao {
            return database.newsDao()
        }

        @OptIn(ExperimentalPagingApi::class)
        @Provides
        @Singleton
        fun provideNewsPager(
            remoteMediator: NewsRemoteMediator,
            newsDao: NewsDao
        ): Pager<Int, NewsEntity> {
            return Pager(
                config = PagingConfig(pageSize = 30, initialLoadSize = 30),
                remoteMediator = remoteMediator,
                pagingSourceFactory = {
                    newsDao.getPagingSource()
                }
            )
        }
    }
}
