package com.tilda.feature.crypto.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CoinPriceDto(
    @field:Json(name = "CLOSE")
    val closingPrice: Double,
    @field:Json(name = "OPEN")
    val openingPrice: Double,
    @field:Json(name = "HIGH")
    val highestPrice: Double,
    @field:Json(name = "LOW")
    val lowestPrice: Double,
    @field:Json(name = "TIMESTAMP")
    val timestamp: Long,
    @field:Json(name = "VOLUME")
    val volume: Double
)
