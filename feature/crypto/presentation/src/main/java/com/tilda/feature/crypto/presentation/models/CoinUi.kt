package com.tilda.feature.crypto.presentation.models

import android.icu.text.NumberFormat
import androidx.compose.runtime.Immutable
import com.tilda.feature.crypto.domain.model.Coin
import com.tilda.feature.crypto.domain.model.CoinPrice
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
    val coinPriceHistory: List<CoinPrice> = emptyList(),
)

@Immutable
data class DisplayableNumber(
    val value: Double,
    val formatted: String,
)


/** Formats a [Double] with two decimal digits for UI display. */
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

/** Maps a domain [Coin] into [CoinUi] with display-ready numeric fields. */
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

/** Prefixes this display value with a dollar sign. */
fun DisplayableNumber.addCurrencySign(): DisplayableNumber {
    return copy(formatted = "$$formatted")
}

internal val previewCoin: CoinUi = Coin(
    id = 1,
    rank = "1",
    symbol = "BTC",
    name = "Bitcoin",
    currentPrice = 57435.28628593438,
    marketCap = 100000000000.0,
    priceChange24h = 10000.0,
    priceChangePercentage24h = 10.0,
    logoUrl = "",
    lastUpdate = 0L
).toCoinUi()
