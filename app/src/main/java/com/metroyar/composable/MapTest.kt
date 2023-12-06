package com.metroyar.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapboxMap
import com.ramcosta.composedestinations.annotation.Destination

@OptIn(MapboxExperimental::class)
@Destination
@Composable
fun MapTest() {
    MapboxMap(
        modifier = Modifier.fillMaxSize(),
        mapInitOptionsFactory = { context ->
            MapInitOptions(
                context = context,
                styleUri = Style.TRAFFIC_DAY,
                cameraOptions = CameraOptions.Builder()
                    .center(Point.fromLngLat(51.388740,35.714045))
                    .zoom(15.0)
                    .build()
            )
        }
    )
}