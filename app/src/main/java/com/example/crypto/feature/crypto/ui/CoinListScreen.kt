package com.example.crypto.feature.crypto.ui

import android.graphics.Color
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.crypto.core.ui.theme.CryptoTheme
import com.example.crypto.feature.crypto.ui.components.CoinListItem
import com.example.crypto.feature.crypto.ui.components.previewCoin

@Composable
fun CoinListScreen(
    uiState: CoinListUiState,
    modifier: Modifier = Modifier
) {
    if (uiState.isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (uiState.coins.isNotEmpty()) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainer,
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp),
            ) {
                items(uiState.coins) {
                    CoinListItem(it)
                }
            }
        }

    }
}

@PreviewLightDark
@Composable
private fun CoinListScreenPreview() {
    CryptoTheme {
        CoinListScreen(
            uiState = CoinListUiState(
                coins = (1..10).map { previewCoin.copy(id = it.toString()) }
            )
        )
    }
}