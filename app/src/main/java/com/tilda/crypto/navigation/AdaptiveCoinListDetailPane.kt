package com.tilda.crypto.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tilda.feature.crypto.presentation.coin_detail.CoinDetailScreen
import com.tilda.feature.crypto.presentation.coin_list.CoinListScreen
import com.tilda.feature.crypto.presentation.coin_list.CoinListViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun AdaptiveCoinListDetailPane(
    modifier: Modifier = Modifier,
    viewModel: CoinListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val listDetailNavigator = rememberListDetailPaneScaffoldNavigator<Any>()
    val scope = rememberCoroutineScope()

    BackHandler(
        enabled = listDetailNavigator.canNavigateBack(),
        onBack = {
            scope.launch {
                listDetailNavigator.navigateBack()
            }
        }
    )

    NavigableListDetailPaneScaffold(
        navigator = listDetailNavigator,
        listPane = {
            AnimatedPane {
                CoinListScreen(
                    state,
                    onItemClick = { item ->
                        viewModel.onCoinClicked(item)
                        scope.launch {
                            listDetailNavigator.navigateTo(
                                ListDetailPaneScaffoldRole.Detail
                            )
                        }
                    },
                    onFavoriteClick = viewModel::onFavoriteClick,
                    onFavoritesFilterChanged = viewModel::onFavoritesFilterChanged
                )
            }
        },
        detailPane = {
            AnimatedPane {
                CoinDetailScreen(
                    state = state,
                    onBackClick = {
                        scope.launch {
                            listDetailNavigator.navigateBack()
                        }
                    },
                    onFavoriteClick = viewModel::onFavoriteClick
                )
            }
        },
        modifier = modifier
    )
}
