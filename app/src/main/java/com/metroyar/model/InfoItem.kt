package com.metroyar.model

import androidx.compose.ui.graphics.vector.ImageVector

data class InfoItem(
    val title: String,
    val icon: ImageVector,
    val endText:String="",
    val onClickedItem: () -> Unit
)
