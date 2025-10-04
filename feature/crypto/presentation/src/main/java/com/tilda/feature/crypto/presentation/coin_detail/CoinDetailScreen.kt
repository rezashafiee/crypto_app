package com.tilda.feature.crypto.presentation.coin_detail

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
import com.tilda.core.presentation.util.LocalDeviceWindowInfo
import com.tilda.feature.crypto.presentation.coin_detail.components.CompactView
import com.tilda.feature.crypto.presentation.coin_list.CoinListUiState
import com.tilda.feature.crypto.presentation.models.previewCoin

@Composable
fun CoinDetailScreen(
    state: CoinListUiState,
    modifier: Modifier = Modifier
) {

    if (state.selectedCoin != null) {
        val coin = state.selectedCoin
        BoxWithConstraints {
            val deviceWindowInfo = LocalDeviceWindowInfo.current
            val isCompact = this.maxWidth < 600.dp

            if (isCompact) {
                CompactView(
                    coin,
                    deviceWindowInfo.foldableInfo.isFoldable,
                    deviceWindowInfo.orientationInfo.isLandscape
                )
            } else {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) {
                    Text(
                        text = "TabletView Under construction",
                        modifier = modifier.align(Alignment.Center),
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