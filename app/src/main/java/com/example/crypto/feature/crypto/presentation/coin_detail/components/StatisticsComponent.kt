package com.example.crypto.feature.crypto.presentation.coin_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.crypto.R
import com.example.crypto.core.presentation.theme.CryptoTheme

@Composable
fun StatisticsComponent(
    marketCap: String,
    volume24h: String,
    popularity: String,
    modifier: Modifier = Modifier,
    lowestPrice: String = "",
    highestPrice: String = "",
    allTimeHigh: String = ""
) {
    Column(
        modifier = modifier
    ) {
        LabelComponent(stringResource(R.string.market_statistics))
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
            ) {
                LabelComponent(stringResource(R.string.market_cap))
                PriceComponent(marketCap)
                Spacer(modifier = Modifier.height(16.dp))
                LabelComponent(stringResource(R.string.low))
                PriceComponent(lowestPrice)
            }
            Column(
                horizontalAlignment = Alignment.Start,
            ) {
                LabelComponent(stringResource(R.string.volume_24h))
                PriceComponent(volume24h)
                Spacer(modifier = Modifier.height(16.dp))
                LabelComponent(stringResource(R.string.high))
                PriceComponent(highestPrice)
            }
            Column(
                horizontalAlignment = Alignment.Start,
            ) {
                LabelComponent(stringResource(R.string.popularity))
                PriceComponent(popularity)
                Spacer(modifier = Modifier.height(16.dp))
                LabelComponent(stringResource(R.string.all_time_high))
                PriceComponent(allTimeHigh)
            }
        }

    }
}


@PreviewLightDark
@Composable
private fun StatisticsComponentPreview() {
    CryptoTheme {
        StatisticsComponent(
            marketCap = "100,000,000,000",
            volume24h = "1,000,000,000",
            popularity = "1",
            lowestPrice = "1,000,000,000",
            highestPrice = "1,000,000,000",
            allTimeHigh = "1,000,000,000",
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer)
        )
    }
}


@Composable
fun LabelComponent(
    text: String,
) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.outline,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
    )
}

@Composable
fun PriceComponent(
    text: String,
) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.onSurface,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
    )
}