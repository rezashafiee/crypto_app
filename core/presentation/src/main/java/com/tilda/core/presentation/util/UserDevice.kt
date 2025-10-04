package com.tilda.core.presentation.util

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import androidx.window.layout.WindowLayoutInfo

@SuppressLint("RestrictedApi")
@Composable
fun rememberWindowSizeInfo(): WindowSizeInfo {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val windowInfoTracker = remember { WindowInfoTracker.getOrCreate(context) }

    val windowLayoutInfo: WindowLayoutInfo by windowInfoTracker
        .windowLayoutInfo(lifecycleOwner as Activity)
        .collectAsState(initial = WindowLayoutInfo(emptyList()))

    // If there is any FoldingFeature, we’re on a foldable device
    val foldingFeature = windowLayoutInfo.displayFeatures
        .filterIsInstance<FoldingFeature>()
        .firstOrNull()

    return WindowSizeInfo(foldingFeature = foldingFeature)
}

data class WindowSizeInfo(
    private val foldingFeature: FoldingFeature?
) {
    val isFoldable: Boolean
        get() = foldingFeature != null
    val isPostured: Boolean
        get() = foldingFeature?.state == FoldingFeature.State.HALF_OPENED

    val isTabletop: Boolean
        get() = isPostured && foldingFeature?.orientation == FoldingFeature.Orientation.HORIZONTAL

    val isBookMode: Boolean
        get() = isPostured && foldingFeature?.orientation == FoldingFeature.Orientation.VERTICAL
}