package com.tilda.feature.crypto.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class CoinDto(
    @field:Json(name = "ID")
    val id: Int,
    @field:Json(name = "TOPLIST_BASE_RANK")
    val rank: Rank,
    @field:Json(name = "SYMBOL")
    val symbol: String,
    @field:Json(name = "NAME")
    val name: String,
    @field:Json(name = "PRICE_USD")
    val priceUsd: Double,
    @field:Json(name = "TOTAL_MKT_CAP_USD")
    val marketCapUsd: Double,
    @field:Json(name = "SPOT_MOVING_24_HOUR_CHANGE_USD")
    val change24Hr: Double,
    @field:Json(name = "SPOT_MOVING_24_HOUR_CHANGE_PERCENTAGE_USD")
    val changePercent24Hr: Double,
    @field:Json(name = "LOGO_URL")
    val logoUrl: String?,
    @field:Json(name = "ASSET_DESCRIPTION_SNIPPET")
    val description: String?,
    @field:Json(name = "PRICE_USD_LAST_UPDATE_TS")
    val lastUpdate: Long,
)

@JsonClass(generateAdapter = true)
internal data class Rank(
    @field:Json(name = "TOTAL_MKT_CAP_USD")
    val marketCapRank: Short
)
