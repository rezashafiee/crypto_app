package com.example.crypto.feature.crypto.data.mappers

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

// Todo: convert dto to entity and vice versa