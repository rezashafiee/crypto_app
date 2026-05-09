package com.tilda.feature.crypto.presentation.models

import androidx.compose.runtime.Immutable
import com.tilda.feature.crypto.domain.model.Coin
import com.tilda.feature.crypto.domain.model.CoinPrice
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.log10

private const val DefaultFractionDigits = 2
private const val SignificantFractionDigits = 2
private const val MaximumPriceFractionDigits = 12

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
    val isFavorite: Boolean = false,
    val coinPriceHistory: List<CoinPrice> = emptyList(),
)

@Immutable
data class DisplayableNumber(
    val value: Double,
    val formatted: String,
)


/** Formats a [Double] with two decimal digits for UI display. */
fun Double.toDisplayableNumber(): DisplayableNumber {
    return DisplayableNumber(
        value = this,
        formatted = formatNumber(
            minimumFractionDigits = DefaultFractionDigits,
            maximumFractionDigits = DefaultFractionDigits
        )
    )
}

/**
 * Formats a USD price for UI display.
 *
 * The displayed precision is derived from the related price change so each coin can show the
 * smallest useful movement without applying a fixed threshold across all prices.
 */
fun Double.toDisplayablePrice(referenceChange: Double = this): DisplayableNumber {
    return DisplayableNumber(
        value = this,
        formatted = formatNumber(
            minimumFractionDigits = DefaultFractionDigits,
            maximumFractionDigits = referenceChange.priceFractionDigits(fallbackValue = this)
        )
    )
}

/** Maps a domain [Coin] into [CoinUi] with display-ready numeric fields. */
fun Coin.toCoinUi(): CoinUi {
    return CoinUi(
        id = id,
        rank = rank,
        name = name,
        symbol = symbol,
        logoUrl = logoUrl,
        currentPrice = currentPrice.toDisplayablePrice(referenceChange = priceChange24h)
            .addCurrencySign(),
        marketCap = marketCap.toDisplayableNumber().addCurrencySign(),
        marketCapShorted = (marketCap / 1000000000).toDisplayableNumber().addCurrencySign(),
        priceChange24h = priceChange24h.toDisplayablePrice(referenceChange = priceChange24h)
            .addCurrencySign(),
        priceChangePercentage24h = priceChangePercentage24h.toDisplayableNumber(),
        isFavorite = isFavorite,
    )
}

/** Prefixes this display value with a dollar sign. */
fun DisplayableNumber.addCurrencySign(): DisplayableNumber {
    return copy(formatted = "$$formatted")
}

private fun Double.formatNumber(
    minimumFractionDigits: Int,
    maximumFractionDigits: Int
): String {
    return NumberFormat.getNumberInstance(Locale.getDefault())
        .apply {
            this.minimumFractionDigits = minimumFractionDigits
            this.maximumFractionDigits = maximumFractionDigits
        }
        .format(this)
}

private fun Double.priceFractionDigits(fallbackValue: Double): Int {
    val referenceValue = abs(this).takeUnless { it == 0.0 } ?: abs(fallbackValue)
    if (!referenceValue.isFinite() || referenceValue == 0.0 || referenceValue >= 1.0) {
        return DefaultFractionDigits
    }

    return (ceil(-log10(referenceValue)).toInt() + SignificantFractionDigits - 1)
        .coerceIn(DefaultFractionDigits, MaximumPriceFractionDigits)
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
