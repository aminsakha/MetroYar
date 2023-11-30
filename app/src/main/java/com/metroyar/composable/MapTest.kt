package com.metroyar.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun MapTest() {
    val context = LocalContext.current
    val key = "wjdrsAFBJTk5FnmDlNnr"
    // Find other maps in https://cloud.maptiler.com/maps/
    val mapId = "streets-v2"
    val styleUrl = "https://api.maptiler.com/maps/$mapId/style.json?key=$key"

    AndroidView(factory = {
        Mapbox.getInstance(context)
        MapView(context)
    }) { mapView ->
        mapView.getMapAsync { map ->
            map.setStyle(styleUrl) {
                map.uiSettings.setAttributionMargins(15, 0, 0, 15)
                map.cameraPosition = CameraPosition.Builder()
                    .target(LatLng(35.714045, 51.388740))
                    .zoom(15.0)
                    .build()
            }
        }
    }
}
