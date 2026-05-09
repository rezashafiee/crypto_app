package com.tilda.feature.crypto.presentation.coin_detail.components

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import coil3.compose.AsyncImage
import com.tilda.core.presentation.components.CoinTitle
import com.tilda.core.presentation.theme.CryptoTheme
import com.tilda.feature.crypto.presentation.R
import com.tilda.feature.crypto.presentation.models.CoinUi
import com.tilda.feature.crypto.presentation.models.previewCoin

@Composable
internal fun CompactView(
    coinUi: CoinUi,
    isFoldable: Boolean,
    isLandscape: Boolean,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer
            )
            .fillMaxSize()
            .verticalScroll(scrollState)
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
            if (!isFoldable && !isLandscape) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_back),
                        contentDescription = stringResource(
                            R.string.content_description_back_button
                        ),
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
            } else {
                Spacer(modifier = Modifier.size(48.dp))
            }
            CoinTitle(
                coinUi.name,
                coinUi.symbol
            )
            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_star),
                    contentDescription = if (coinUi.isFavorite) {
                        "Remove from watchlist"
                    } else {
                        "Add to watchlist"
                    },
                    modifier = Modifier.size(24.dp),
                    tint = if (coinUi.isFavorite) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outline
                    }
                )
            }
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
            AsyncImage(
                model = coinUi.logoUrl,
                contentDescription = "${coinUi.name} logo",
                modifier = Modifier.size(56.dp)
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        ChartComponent(coinUi, modifier)
        Spacer(modifier = Modifier.height(32.dp))
        CoinMarketStatistics(
            coinUi = coinUi,
            modifier = Modifier.widthIn(max = 400.dp)
        )
    }
}

@PreviewLightDark
@Composable
private fun CompactViewPreview() {
    CryptoTheme {
        CompactView(
            coinUi = previewCoin,
            isFoldable = false,
            isLandscape = false,
            onBackClick = {},
            onFavoriteClick = {}
        )
    }
}
