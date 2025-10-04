package com.tilda.feature.crypto.presentation.coin_detail.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.candlestickSeries
import com.tilda.core.presentation.theme.CryptoTheme
import com.tilda.feature.crypto.presentation.models.CoinUi
import com.tilda.feature.crypto.presentation.models.previewCoin

@Composable
fun ChartComponent(
    coin: CoinUi,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        AnimatedVisibility(
            visible = coin.coinPriceHistory.isNotEmpty()
        ) {
            val modelProducer = remember { CartesianChartModelProducer() }
            LaunchedEffect(Unit) {
                modelProducer.runTransaction {
                    candlestickSeries(
                        x = coin.coinPriceHistory.map { it.dateTime.hour },
                        opening = coin.coinPriceHistory.map { it.openingPrice },
                        closing = coin.coinPriceHistory.map { it.closingPrice },
                        low = coin.coinPriceHistory.map { it.lowestPrice },
                        high = coin.coinPriceHistory.map { it.highestPrice }
                    )
                }
            }
            CandlestickChart(
                modelProducer = modelProducer,
                modifier = modifier
            )
        }
    }
}

@Preview
@Composable
private fun ChartComponentPreview() {
    CryptoTheme {
        ChartComponent(
            previewCoin.copy()
        )
    }
}