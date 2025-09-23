package com.tilda.feature.crypto.presentation.models

import android.icu.text.NumberFormat
import androidx.compose.runtime.Immutable
import com.tilda.feature.crypto.domain.model.Coin
import java.util.Locale

@Immutable
data class CoinUi(
    val id: Int,
    val rank: String,
    val name: String,
    val symbol: String,
    val logoUrl: String,
    val currentPrice: DisplayableNumber,
    val marketCap: DisplayableNumber,
    val marketCapShorted: DisplayableNumber,
    val priceChange24h: DisplayableNumber,
    val priceChangePercentage24h: DisplayableNumber,
)

@Immutable
data class DisplayableNumber(
    val value: Double,
    val formatted: String,
)


fun Double.toDisplayableNumber(): DisplayableNumber {
    val formatted = NumberFormat.getNumberInstance(Locale.getDefault())
        .apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }.format(this)
    return DisplayableNumber(
        value = this,
        formatted = formatted
    )
}

fun Coin.toCoinUi() = CoinUi(
    id = id,
    rank = rank,
    name = name,
    symbol = symbol,
    logoUrl = logoUrl,
    currentPrice = currentPrice.toDisplayableNumber().addCurrencySign(),
    marketCap = marketCap.toDisplayableNumber().addCurrencySign(),
    marketCapShorted = (marketCap / 1000000000).toDisplayableNumber().addCurrencySign(),
    priceChange24h = priceChange24h.toDisplayableNumber().addCurrencySign(),
    priceChangePercentage24h = priceChangePercentage24h.toDisplayableNumber(),
)

fun DisplayableNumber.addCurrencySign(): DisplayableNumber {
    return copy(formatted = "$$formatted")
}