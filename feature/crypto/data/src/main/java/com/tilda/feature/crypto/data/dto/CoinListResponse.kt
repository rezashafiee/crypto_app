package com.tilda.feature.crypto.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class CoinListResponse(
    @field:Json(name = "Data")
    val data: Data
)

@JsonClass(generateAdapter = true)
internal data class Data(
    @field:Json(name = "LIST")
    val list: List<CoinDto>
)
