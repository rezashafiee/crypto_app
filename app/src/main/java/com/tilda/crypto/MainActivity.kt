package com.tilda.crypto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.tilda.core.presentation.theme.CryptoTheme
import com.tilda.core.presentation.util.DeviceWindowInfo
import com.tilda.core.presentation.util.LocalDeviceWindowInfo
import com.tilda.core.presentation.util.rememberFoldableInfo
import com.tilda.core.presentation.util.rememberScreenOrientation
import com.tilda.crypto.navigation.AdaptiveCoinListDetailPane

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CryptoTheme {
                val screenOrientation = rememberScreenOrientation()
                val foldableInfo = rememberFoldableInfo()

                val deviceWindowInfo = DeviceWindowInfo(
                    foldableInfo = foldableInfo,
                    orientationInfo = screenOrientation
                )

                CompositionLocalProvider(LocalDeviceWindowInfo provides deviceWindowInfo) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        AdaptiveCoinListDetailPane(
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}
