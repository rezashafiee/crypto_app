package com.tilda.feature.crypto.presentation.coin_detail.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tilda.core.presentation.theme.CryptoTheme
import com.tilda.feature.crypto.presentation.coin_detail.ChartStyle
import com.tilda.feature.crypto.presentation.coin_detail.DataPoint
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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.size(400.dp, 300.dp)
    ) {
        AnimatedVisibility(
            visible = coin.coinPriceHistory.isNotEmpty()
        ) {
            var selectedDataPoint by remember {
                mutableStateOf<DataPoint?>(null)
            }
            var labelWidth by remember {
                mutableFloatStateOf(0f)
            }
            var totalChartWidth by remember {
                mutableFloatStateOf(0f)
            }
            val amountOfVisibleDataPoints = if (labelWidth > 0) {
                ((totalChartWidth - 2.5 * labelWidth) / labelWidth).toInt()
            } else {
                0
            }
            val startIndex = (coin.coinPriceHistory.lastIndex - amountOfVisibleDataPoints)
                .coerceAtLeast(0)
            LineChart(
                dataPoints = coin.coinPriceHistory,
                style = ChartStyle(
                    chartLineColor = MaterialTheme.colorScheme.primary,
                    unselectedColor = MaterialTheme.colorScheme.secondary.copy(
                        alpha = 0.3f
                    ),
                    selectedColor = MaterialTheme.colorScheme.primary,
                    helperLinesThicknessPx = 5f,
                    axisLinesThicknessPx = 5f,
                    labelFontSize = 14.sp,
                    minYLabelSpacing = 25.dp,
                    verticalPadding = 8.dp,
                    horizontalPadding = 8.dp,
                    xAxisLabelSpacing = 8.dp
                ),
                visibleDataPointsIndices = startIndex..coin.coinPriceHistory.lastIndex,
                unit = "$",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f)
                    .onSizeChanged { totalChartWidth = it.width.toFloat() },
                selectedDataPoint = selectedDataPoint,
                onSelectedDataPoint = {
                    selectedDataPoint = it
                },
                onXLabelWidthChange = { labelWidth = it }
            )
        }
    }
}

@Preview(widthDp = 1000)
@Composable
private fun ChartComponentPreview() {
    CryptoTheme {
        ChartComponent(
            previewCoin
        )
    }
}