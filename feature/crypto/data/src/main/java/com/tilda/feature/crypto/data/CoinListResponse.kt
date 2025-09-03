package com.tilda.feature.crypto.data

import com.tilda.feature.crypto.data.dto.CoinDto
import kotlinx.serialization.Serializable

@Serializable
data class CoinListResponse(
    val data: List<CoinDto>
)
