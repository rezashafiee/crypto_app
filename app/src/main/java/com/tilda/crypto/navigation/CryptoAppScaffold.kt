package com.tilda.crypto.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.tilda.core.presentation.util.LocalDeviceWindowInfo

private enum class TopLevelDestination(
    val label: String,
    val icon: ImageVector
) {
    Coins(
        label = "Coins",
        icon = Icons.AutoMirrored.Filled.ShowChart
    ),
    News(
        label = "News",
        icon = Icons.AutoMirrored.Filled.Article
    )
}

@Composable
fun CryptoAppScaffold(
    modifier: Modifier = Modifier
) {
    var selectedDestination by rememberSaveable { mutableStateOf(TopLevelDestination.Coins) }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val deviceWindowInfo = LocalDeviceWindowInfo.current
        val useNavigationRail = maxWidth >= 600.dp || deviceWindowInfo.foldableInfo.isFoldable

        Scaffold(
            bottomBar = {
                if (!useNavigationRail) {
                    CryptoBottomNavigation(
                        selectedDestination = selectedDestination,
                        onDestinationSelected = { selectedDestination = it }
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            if (useNavigationRail) {
                Row(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                ) {
                    CryptoNavigationRail(
                        selectedDestination = selectedDestination,
                        onDestinationSelected = { selectedDestination = it }
                    )
                    VerticalDivider(
                        modifier = Modifier.fillMaxHeight(),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                    DestinationContent(
                        selectedDestination = selectedDestination,
                        modifier = Modifier.weight(1f)
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    DestinationContent(
                        selectedDestination = selectedDestination,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun CryptoBottomNavigation(
    selectedDestination: TopLevelDestination,
    onDestinationSelected: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        TopLevelDestination.entries.forEach { destination ->
            NavigationBarItem(
                selected = destination == selectedDestination,
                onClick = { onDestinationSelected(destination) },
                icon = { DestinationIcon(destination) },
                label = { Text(destination.label) }
            )
        }
    }
}

@Composable
private fun CryptoNavigationRail(
    selectedDestination: TopLevelDestination,
    onDestinationSelected: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationRail(modifier = modifier.fillMaxHeight()) {
        TopLevelDestination.entries.forEach { destination ->
            NavigationRailItem(
                selected = destination == selectedDestination,
                onClick = { onDestinationSelected(destination) },
                icon = { DestinationIcon(destination) },
                label = { Text(destination.label) }
            )
        }
    }
}

@Composable
private fun DestinationIcon(destination: TopLevelDestination) {
    Icon(
        imageVector = destination.icon,
        contentDescription = null
    )
}

@Composable
private fun DestinationContent(
    selectedDestination: TopLevelDestination,
    modifier: Modifier = Modifier
) {
    when (selectedDestination) {
        TopLevelDestination.Coins -> AdaptiveCoinListDetailPane(modifier = modifier)
        TopLevelDestination.News -> AdaptiveNewsListDetailPane(modifier = modifier)
    }
}
