package com.metroyar.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class InfoItem(
    val title: String,
    val icon: ImageVector,
    val onClickedScreen: @Composable () -> Unit
)
