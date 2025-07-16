package com.example.crypto.feature.crypto.domain

data class Coin(
    val id: String,
    val rank: String,
    val symbol: String,
    val name: String,
    val currentPrice: Double,
    val marketCap: Double,
    val priceChange24h: Double,
    val priceChangePercentage24h: Double
)
