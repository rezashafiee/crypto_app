package com.tilda.feature.news.data.mapper

import com.tilda.feature.news.data.dto.NewsArticleDto
import com.tilda.feature.news.data.local.NewsEntity
import com.tilda.feature.news.domain.model.NewsArticle
import java.time.Instant
import java.time.ZoneId

private const val CATEGORY_SEPARATOR = "||"

fun NewsArticleDto.toNewsArticle(): NewsArticle {
    val fallbackId = listOfNotNull(
        guid,
        id?.toString(),
        url,
        title
    ).firstOrNull().orEmpty()

    return NewsArticle(
        id = fallbackId,
        title = title.orEmpty(),
        body = body.orEmpty().trim(),
        authors = authors.orEmpty(),
        source = sourceData?.name.orEmpty().ifBlank { "CoinDesk" },
        url = url.orEmpty(),
        imageUrl = imageUrl.orEmpty(),
        publishedOn = Instant
            .ofEpochSecond(publishedOn ?: 0L)
            .atZone(ZoneId.systemDefault()),
        categories = categoryData
            ?.mapNotNull { it.name ?: it.category }
            ?.filter { it.isNotBlank() }
            .orEmpty()
    )
}

fun NewsArticle.toNewsEntity(
    cachedAtEpochSeconds: Long = System.currentTimeMillis() / 1000
): NewsEntity {
    return NewsEntity(
        id = id,
        title = title,
        body = body,
        authors = authors,
        source = source,
        url = url,
        imageUrl = imageUrl,
        publishedOnEpochSeconds = publishedOn.toEpochSecond(),
        categories = categories.joinToString(CATEGORY_SEPARATOR),
        cachedAtEpochSeconds = cachedAtEpochSeconds
    )
}

fun NewsEntity.toNewsArticle(): NewsArticle {
    return NewsArticle(
        id = id,
        title = title,
        body = body,
        authors = authors,
        source = source,
        url = url,
        imageUrl = imageUrl,
        publishedOn = Instant
            .ofEpochSecond(publishedOnEpochSeconds)
            .atZone(ZoneId.systemDefault()),
        categories = categories
            .split(CATEGORY_SEPARATOR)
            .filter { it.isNotBlank() }
    )
}
