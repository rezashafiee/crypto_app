package com.example.crypto.feature.crypto.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.crypto.core.ui.theme.CryptoTheme
import com.example.crypto.core.ui.theme.greenDark
import com.example.crypto.core.ui.theme.greenLight
import com.example.crypto.feature.crypto.domain.Coin
import com.example.crypto.feature.crypto.ui.models.CoinUi
import com.example.crypto.feature.crypto.ui.models.toCoinUi

@Composable
fun CoinListItem(
    coinUi: CoinUi,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Text(
                text = coinUi.rank,
                color = MaterialTheme.colorScheme.outline,
                fontWeight = FontWeight.Black,
                fontSize = 12.sp,
            )
            Image(
                imageVector = ImageVector.vectorResource(coinUi.iconRes),
                contentDescription = "${coinUi.name} logo",
                modifier = Modifier.size(48.dp)
            )
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            )
            {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            append(coinUi.name)
                        }
                        append(" ")
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.outline
                            )
                        ) {
                            append("(${coinUi.symbol})")
                        }
                    }
                )
                Text(
                    text = "${coinUi.marketCapShorted.formatted} Billions",
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = coinUi.currentPrice.formatted,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                )
                Text(
                    text = "${coinUi.priceChange24h.formatted} (${coinUi.priceChangePercentage24h.formatted}%)",
                    color = if (coinUi.priceChange24h.value > 0) {
                        if (isSystemInDarkTheme()) greenDark else greenLight
                    } else {
                        MaterialTheme.colorScheme.error
                    },
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                )
            }
        }
    }
}


@PreviewLightDark
@Composable
private fun CoinListItemPreview() {
    CryptoTheme {
        CoinListItem(
            previewCoin
        )
    }
}

internal val previewCoin: CoinUi = Coin(
    id = "bitcoin",
    rank = "1",
    symbol = "BTC",
    name = "Bitcoin",
    currentPrice = 57435.28628593438,
    marketCap = 100000000000.0,
    priceChange24h = 10000.0,
    priceChangePercentage24h = 10.0
).toCoinUi()
