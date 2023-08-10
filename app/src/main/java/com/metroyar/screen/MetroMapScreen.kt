package com.metroyar.screen


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.metroyar.R
import net.engawapg.lib.zoomable.ZoomState
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

@Composable
fun MetroMapScreen() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        ZoomableImage(drawable = R.drawable.metro_map)
    }
}

@Composable
fun ZoomableImage(drawable: Int) {
    val painter = painterResource(id = drawable)
    val zoomState = rememberZoomState(contentSize = painter.intrinsicSize)
    Image(
        painter = painter,
        contentDescription = "Zoomable image",
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .fillMaxSize()
            .zoomable(zoomState),
    )
}