package com.example.crypto.feature.crypto.presentation.coin_detail.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.sp
import com.example.crypto.R
import com.example.crypto.core.presentation.theme.CryptoTheme
import com.example.crypto.core.presentation.theme.greenDark
import com.example.crypto.core.presentation.theme.greenLight
import com.example.crypto.feature.crypto.presentation.models.DisplayableNumber

@Composable
fun PriceChangeText(
    priceChange: DisplayableNumber,
    priceChangePercentage24h: DisplayableNumber
) {
    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            ) {
                append(stringResource(R.string.last_24hrs))
            }
            append(" ")
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp,
                    color = if (priceChange.value > 0) {
                        if (isSystemInDarkTheme()) greenDark else greenLight
                    } else {
                        MaterialTheme.colorScheme.error
                    },
                )
            ) {
                val positiveSign = if (priceChange.value > 0) "+" else ""
                append("$positiveSign${priceChange.formatted} ($positiveSign${priceChangePercentage24h.formatted}%)")
            }
        }
    )
}


@PreviewLightDark
@Composable
private fun PriceChangeTextPreview() {
    CryptoTheme {
        PriceChangeText(
            priceChange = DisplayableNumber(
                value = 10000.0,
                formatted = "10,000.00"
            ),
            priceChangePercentage24h = DisplayableNumber(
                value = 10.0,
                formatted = "10.00"
            )
        )
    }
}