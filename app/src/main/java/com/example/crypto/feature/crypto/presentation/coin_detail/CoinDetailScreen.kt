package com.example.crypto.feature.crypto.presentation.coin_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.crypto.R
import com.example.crypto.core.presentation.components.CoinTitle
import com.example.crypto.core.presentation.components.LoadingView
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