package com.example.crypto.feature.crypto.presentation.coin_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.crypto.core.presentation.components.LoadingView
import com.example.crypto.core.presentation.theme.CryptoTheme
import com.example.crypto.feature.crypto.presentation.coin_list.components.CoinListItem
import com.example.crypto.feature.crypto.presentation.coin_list.components.previewCoin
import com.example.crypto.feature.crypto.presentation.models.CoinUi

@Composable
fun CoinListScreen(
    uiState: CoinListUiState,
    modifier: Modifier = Modifier,
    onItemClick: (coin: CoinUi) -> Unit = {}
) {
    if (uiState.isLoading) {
        LoadingView(modifier)
    } else if (uiState.coins.isNotEmpty()) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp),
            modifier = modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surfaceContainer)
        ) {
            items(uiState.coins, key = { coinUi -> coinUi.id }) {
                CoinListItem(
                    coinUi = it,
                    modifier = Modifier.clickable(onClick = { onItemClick(it) })
                )
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