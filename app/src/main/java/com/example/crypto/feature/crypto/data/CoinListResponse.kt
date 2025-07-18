package com.example.crypto.feature.crypto.data

import com.example.crypto.feature.crypto.data.dto.CoinDto
import kotlinx.serialization.Serializable

@Serializable
data class CoinListResponse(
    val data: List<CoinDto>
)
