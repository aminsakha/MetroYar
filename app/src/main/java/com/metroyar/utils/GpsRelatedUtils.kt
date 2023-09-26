package com.metroyar.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.metroyar.model.GPSCoordinate
import com.metroyar.network.MetroYarNeshanApiService

fun checkGpsStatus(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

suspend fun getClosestStationsFromApi(
    location: GPSCoordinate,
    onPairChange: (Pair<String, String>) -> Unit
) {
    try {
        val response =
            MetroYarNeshanApiService.retrofitService.findNearestStationsFromApi(
                latitude = location.y,
                longitude = location.x,
                term = "ایستگاه مترو"
            )
        val filteredList = response.items.filter { it.type == "subway_station" }
        val pair = Pair(
            filteredList.getOrNull(0)?.title ?: "",
            filteredList.getOrNull(1)?.title ?: ""
        )
        onPairChange.invoke(pair)
    } catch (_: Exception) {
    }
}

fun convertNeshanStationNameToMyFormat(pair: Pair<String, String>): Pair<String, String> {
    val matchingNames = GlobalObjects.stationList.map { it.stationName }
        .filter {
            pair.first.contains(it) || pair.second.contains(it)
        }

    return Pair(matchingNames.getOrNull(0) ?: "", matchingNames.getOrNull(1) ?: "")
}

@SuppressLint("MissingPermission")
@Composable
fun GetCurrentLocation(context: Context, onLocationChange: (GPSCoordinate) -> Unit) {
    val locationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }
    LaunchedEffect(key1 = Unit) {
        try {
            val priority = Priority.PRIORITY_HIGH_ACCURACY
            locationClient.getCurrentLocation(
                priority,
                CancellationTokenSource().token,
            ).addOnSuccessListener { location ->
                if (location != null) {
                    onLocationChange.invoke(
                        GPSCoordinate(
                            location.longitude,
                            location.latitude
                        )
                    )
                    log("loc", location)
                }
            }
        } catch (_: Exception) {
        }
    }
}