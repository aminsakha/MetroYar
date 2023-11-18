package com.metroyar.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.ramcosta.composedestinations.annotation.Destination
import org.neshan.common.model.LatLng
import org.neshan.mapsdk.MapView
import org.neshan.mapsdk.model.Marker

@Destination
@Composable
fun NeshanMapView() {
    val context = LocalContext.current

    var marker: Marker? = null

    val markerRemember = remember { mutableStateOf(marker) }

    Surface() {
        Row() {
            Column() {
                AndroidView(
                    factory = {
                        MapView(context)
                    },
                    update = {
                        if (markerRemember.value != null) {
                            it.addMarker(markerRemember.value)
                            markerRemember.value = null
                        }
                    },
                    modifier = Modifier.weight(1f),
                )
                ElevatedButton(
                    onClick = {
                       // marker = createMarker(context, LatLng(35.12345, 52.12345))
                        markerRemember.value = marker
                    }
                ) {
                    Text("Add marker")
                }
            }
        }
    }
}
