package com.example.crypto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.crypto.core.presentation.theme.CryptoTheme
import com.example.crypto.feature.crypto.presentation.coin_detail.CoinDetailScreen
import com.example.crypto.feature.crypto.presentation.coin_list.CoinListScreen
import com.example.crypto.feature.crypto.presentation.coin_list.CoinListUiState
import com.example.crypto.feature.crypto.presentation.coin_list.CoinListViewModel
import com.example.crypto.feature.crypto.presentation.models.CoinUi
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CryptoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel by viewModel<CoinListViewModel>()
                    val state = viewModel.state.collectAsStateWithLifecycle()

                    val listDetailNavigator = rememberListDetailPaneScaffoldNavigator<CoinUi>()
                    val scope = rememberCoroutineScope()

                    NavigableListDetailPaneScaffold(
                        navigator = listDetailNavigator,
                        listPane = {
                            AnimatedPane {
                                CoinListScreen(
                                    state.value,
                                    onItemClick = {
                                        scope.launch {
                                            listDetailNavigator.navigateTo(
                                                ListDetailPaneScaffoldRole.Detail,
                                                state.value.selectedCoin
                                            )
                                        }
                                    }
                                )
                            }
                        },
                        detailPane = {
                            AnimatedPane {
                                listDetailNavigator.currentDestination?.contentKey?.let {
                                    CoinDetailScreen(
                                        state = state.value,
                                        windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
                                    )
                                }
                            }
                        }
                    )

                    CoinListScreen(
                        state.value,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}