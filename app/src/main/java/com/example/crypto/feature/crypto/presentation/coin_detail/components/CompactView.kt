package com.example.crypto.feature.crypto.presentation.coin_detail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.crypto.R
import com.example.crypto.core.presentation.components.CoinTitle
import com.example.crypto.core.presentation.theme.CryptoTheme
import com.example.crypto.feature.crypto.presentation.coin_list.components.previewCoin
import com.example.crypto.feature.crypto.presentation.models.CoinUi

@Composable
fun CompactView(
    coinUi: CoinUi,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer
            )
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = modifier
                .background(color = MaterialTheme.colorScheme.surfaceContainer)
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_back),
                contentDescription = stringResource(R.string.content_description_back_button),
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.outline
            )
            CoinTitle(
                coinUi.name,
                coinUi.symbol
            )
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_star),
                contentDescription = "Favorite button",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.outline
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "${coinUi.name} Price",
                    color = MaterialTheme.colorScheme.outline,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                )
                Text(
                    text = coinUi.currentPrice.formatted,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                )
                PriceChangeText(
                    priceChange = coinUi.priceChange24h,
                    priceChangePercentage24h = coinUi.priceChangePercentage24h
                )
            }
            Image(
                imageVector = ImageVector.vectorResource(coinUi.iconRes),
                contentDescription = "${coinUi.name} logo",
                modifier = Modifier.size(56.dp)
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        ChartComponent()
        Spacer(modifier = Modifier.height(32.dp))
        StatisticsComponent(
            coinUi.marketCapShorted.formatted,
            "15.1B",
            "#${coinUi.rank}",
            lowestPrice = "1,000,000",
            highestPrice = "1,000,000",
            allTimeHigh = "5,000,000",
            modifier = Modifier
                .widthIn(max = 400.dp)
        )
    }
}

@PreviewLightDark
@Composable
private fun CompactViewPreview() {
    CryptoTheme {
        CompactView(
            previewCoin
        )
    }
}