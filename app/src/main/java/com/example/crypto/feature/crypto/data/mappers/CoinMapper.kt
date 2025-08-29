package com.example.crypto.feature.crypto.data.mappers

import com.example.crypto.core.data.db.model.CoinEntity
import com.example.crypto.feature.crypto.data.dto.CoinDto
import com.example.crypto.feature.crypto.domain.Coin

fun CoinDto.toCoin() = Coin(
    id = id,
    name = name,
    currentPrice = priceUsd,
    priceChangePercentage24h = changePercent24Hr,
    priceChange24h = priceChange(priceUsd, changePercent24Hr),
    marketCap = marketCapUsd,
    rank = rank,
    symbol = symbol
)


fun priceChange(price: Double, changePercent: Double): Double {
    return price * changePercent
}

fun CoinDto.toCoinEntity() = CoinEntity(
    id = id,
    name = name,
    currentPrice = priceUsd,
    priceChangePercentage24h = changePercent24Hr,
    priceChange24h = priceChange(priceUsd, changePercent24Hr),
    marketCap = marketCapUsd,
    rank = rank,
    symbol = symbol
)

fun CoinEntity.toCoinDto() = CoinDto(
    id = id,
    name = name,
    priceUsd = currentPrice,
    changePercent24Hr = priceChangePercentage24h,
    marketCapUsd = marketCap,
    rank = rank,
    symbol = symbol
)

fun CoinEntity.toCoin() = Coin(
    id = id,
    name = name,
    currentPrice = currentPrice,
    priceChangePercentage24h = priceChangePercentage24h,
    priceChange24h = priceChange24h,
    marketCap = marketCap,
    rank = rank,
    symbol = symbol
)

fun Coin.toCoinEntity() = CoinEntity(
    id = id,
    name = name,
    currentPrice = currentPrice,
    priceChangePercentage24h = priceChangePercentage24h,
    priceChange24h = priceChange24h,
    marketCap = marketCap,
    rank = rank,
    symbol = symbol
)