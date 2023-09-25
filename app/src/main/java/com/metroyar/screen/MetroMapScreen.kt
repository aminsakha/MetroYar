package com.metroyar.screen


import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.metroyar.R
import com.metroyar.utils.BackPressAction
import kotlinx.coroutines.delay
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
        contentDescription = "Zoomable Metro Map",
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .fillMaxSize()
            .zoomable(zoomState),
    )
    BackPressAction()
}
