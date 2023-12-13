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
        val filteredList =
            response.items.filter { it.type == "subway_station" }.distinctBy { it.title }
                .map { element -> convertNeshanStationNameToMyFormat(element.title) }
                .filter { it.isNotEmpty() }

        val pair = Pair(
            filteredList.getOrNull(0) ?: "",
            filteredList.getOrNull(1) ?: ""
        )
        onPairChange.invoke(pair)
    } catch (e: Exception) {
        onPairChange.invoke(Pair("🤔","🤔"))
        e.printStackTrace()
    }
}

fun convertNeshanStationNameToMyFormat(neshanStation: String): String {
    GlobalObjects.stationList.map { it.stationName }.forEach { stationName ->
        if (neshanStation == "ایستگاه مترو دکترشریعتی")
            return "دکتر شریعتی"
        if (neshanStation.contains(stationName) ||
            neshanStation.contains(
                stationName.dropLast(1).replace("ی", "ي") + stationName.last()
            ) ||
            neshanStation.contains(stationName.replace("ی", "ي"))
        ) {
            return stationName
        }
    }
    return ""
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
                else{
                    onLocationChange.invoke(
                        GPSCoordinate(
                            1.1,
                            1.1
                        )
                    )
                }
            }
        } catch (_: Exception) {
            onLocationChange.invoke(
                GPSCoordinate(
                    1.1,
                  1.1
                )
            )
            log("loc", "0 loc")
        }
    }
}
