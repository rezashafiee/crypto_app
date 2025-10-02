package com.tilda.feature.crypto.presentation.models

import android.icu.text.NumberFormat
import androidx.compose.runtime.Immutable
import com.tilda.feature.crypto.domain.model.Coin
import com.tilda.feature.crypto.domain.model.CoinPrice
import com.tilda.feature.crypto.presentation.coin_detail.DataPoint
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.random.Random

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
    val coinPriceHistory: List<DataPoint> = emptyList(),
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


val coinHistoryRandomized =
    (1..20).map {
        CoinPrice(
            priceUsd = Random.nextFloat() * 1000.0,
            dateTime = ZonedDateTime.now().plusHours(it.toLong())
        )
    }

val dataPoints = coinHistoryRandomized.map {
    DataPoint(
        x = it.dateTime.hour.toFloat(),
        y = it.priceUsd.toFloat(),
        xLabel = DateTimeFormatter
            .ofPattern("ha\nM/d")
            .format(it.dateTime)
    )
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
).toCoinUi().copy(coinPriceHistory = dataPoints)