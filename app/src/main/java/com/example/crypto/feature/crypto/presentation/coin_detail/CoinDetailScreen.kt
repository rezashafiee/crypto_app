package com.example.crypto.feature.crypto.presentation.coin_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.crypto.core.presentation.components.LoadingView
import com.example.crypto.feature.crypto.presentation.coin_detail.components.CompactView
import com.example.crypto.feature.crypto.presentation.coin_list.CoinListUiState
import com.example.crypto.feature.crypto.presentation.coin_list.components.previewCoin

@Composable
fun CoinDetailScreen(
    state: CoinListUiState,
    modifier: Modifier = Modifier
) {

    if (state.isLoading) {
        LoadingView(modifier)
    } else if (state.selectedCoin != null) {
        val coin = state.selectedCoin
        BoxWithConstraints {
            if (this.maxWidth < 600.dp) {
                CompactView(coin)
            } else {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) {
                    Text(
                        text = "TabletView Under construction",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun CoinDetailScreenPreview() {
    CoinDetailScreen(
        state = CoinListUiState(
            selectedCoin = previewCoin
        )
    )
}