package com.tilda.feature.crypto.presentation.coin_detail.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tilda.feature.crypto.presentation.models.CoinUi
import com.tilda.feature.crypto.presentation.models.addCurrencySign
import com.tilda.feature.crypto.presentation.models.toDisplayableNumber
import com.tilda.feature.crypto.presentation.models.toDisplayablePrice

@Composable
internal fun CoinMarketStatistics(
    coinUi: CoinUi,
    modifier: Modifier = Modifier
) {
    if (coinUi.coinPriceHistory.isEmpty()) return

    StatisticsComponent(
        marketCap = coinUi.marketCapShorted.formatted,
        volume24h = coinUi.coinPriceHistory.sumOf { it.volume }
            .toDisplayableNumber().formatted,
        popularity = "#${coinUi.rank}",
        lowestPrice = coinUi.coinPriceHistory.minOf { it.lowestPrice }
            .toDisplayablePrice(referenceChange = coinUi.priceChange24h.value)
            .addCurrencySign().formatted,
        highestPrice = coinUi.coinPriceHistory.maxOf { it.highestPrice }
            .toDisplayablePrice(referenceChange = coinUi.priceChange24h.value)
            .addCurrencySign().formatted,
        modifier = modifier
    )
}
