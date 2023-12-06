package com.metroyar.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.ramcosta.composedestinations.annotation.Destination


@Destination
@Composable
fun MapTest() {
    val context = LocalContext.current
    MapboxMap(
        Modifier.fillMaxSize(),
        mapViewportState = MapViewportState().apply {
            setCameraOptions {
                zoom(15.0)
                center(Point.fromLngLat( 51.388740,35.714045))
                pitch(0.0)
                bearing(0.0)
            }
        },
    )
}