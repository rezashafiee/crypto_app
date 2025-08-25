package com.example.crypto.core.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CoinEntity(
    @PrimaryKey val id: String,
    val rank: String,
    val symbol: String,
    val name: String,
    val currentPrice: Double,
    val marketCap: Double,
    val priceChange24h: Double,
    val priceChangePercentage24h: Double
)