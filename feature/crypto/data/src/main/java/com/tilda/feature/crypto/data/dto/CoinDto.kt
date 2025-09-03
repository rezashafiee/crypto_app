package com.tilda.feature.crypto.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CoinDto(
    val id: String,
    val rank: String,
    val symbol: String,
    val name: String,
    val priceUsd: Double,
    val marketCapUsd: Double,
    val changePercent24Hr: Double,
)
