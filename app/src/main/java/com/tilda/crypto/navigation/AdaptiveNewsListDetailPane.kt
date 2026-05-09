package com.tilda.crypto.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tilda.feature.news.presentation.news.CryptoNewsScreen
import com.tilda.feature.news.presentation.news.CryptoNewsViewModel

@Composable
fun AdaptiveNewsListDetailPane(
    modifier: Modifier = Modifier,
    viewModel: CryptoNewsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CryptoNewsScreen(
        state = state,
        onArticleClick = viewModel::onArticleClicked,
        onBackClick = viewModel::clearSelectedArticle,
        modifier = modifier
    )
}
