package com.tilda.feature.crypto.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinPriceDto(
    @SerialName("CLOSE")
    val priceUsd: Double,
    @SerialName("TIMESTAMP")
    val time: Long
)