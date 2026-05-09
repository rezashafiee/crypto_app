package com.tilda.feature.crypto.presentation.coin_detail

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.tilda.core.presentation.util.LocalDeviceWindowInfo
import com.tilda.feature.crypto.presentation.coin_detail.components.CompactView
import com.tilda.feature.crypto.presentation.coin_detail.components.TabletView
import com.tilda.feature.crypto.presentation.coin_list.CoinListUiState
import com.tilda.feature.crypto.presentation.models.CoinUi
import com.tilda.feature.crypto.presentation.models.previewCoin

@Composable
fun CoinDetailScreen(
    state: CoinListUiState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onFavoriteClick: (CoinUi) -> Unit = {}
) {

    if (state.selectedCoin != null) {
        val coin = state.selectedCoin
        BoxWithConstraints {
            val deviceWindowInfo = LocalDeviceWindowInfo.current
            val isCompact = this.maxWidth < 600.dp
            val showBackButton = !deviceWindowInfo.foldableInfo.isFoldable &&
                !deviceWindowInfo.orientationInfo.isLandscape

            if (isCompact) {
                CompactView(
                    coin,
                    deviceWindowInfo.foldableInfo.isFoldable,
                    deviceWindowInfo.orientationInfo.isLandscape,
                    onBackClick = onBackClick,
                    onFavoriteClick = { onFavoriteClick(coin) }
                )
            } else {
                TabletView(
                    coinUi = coin,
                    showBackButton = showBackButton,
                    onBackClick = onBackClick,
                    onFavoriteClick = { onFavoriteClick(coin) },
                    modifier = modifier
                )
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
