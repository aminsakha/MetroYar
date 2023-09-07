package com.metroyar.component_composable

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CircularProgressBar(visible: Boolean) {
    var isVisible by remember { mutableStateOf(visible) }
    if (isVisible) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(48.dp)
                .padding(12.dp)
        )
    }
}