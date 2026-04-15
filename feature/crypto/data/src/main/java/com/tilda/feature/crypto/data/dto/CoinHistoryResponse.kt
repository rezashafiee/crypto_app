package com.tilda.feature.crypto.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CoinHistoryResponse(
    @field:Json(name = "Data")
    val data: List<CoinPriceDto>
)