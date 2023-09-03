package com.metroyar.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.metroyar.R
import com.metroyar.composable.CircularProgressBar
import com.metroyar.composable.SuggestionStationsDialog
import com.metroyar.model.Location
import com.metroyar.model.Station
import com.metroyar.network.MetroYarNeshanApiService
import com.metroyar.screen.EnableLocationDialog
import com.metroyar.screen.PermissionScreen
import com.metroyar.utils.GlobalObjects.TAG
import com.metroyar.utils.GlobalObjects.UserLatitude
import com.metroyar.utils.GlobalObjects.metroGraph
import com.metroyar.utils.GlobalObjects.stationList
import kotlin.system.measureTimeMillis


fun initiateStationsAndAdjNodesLineNum(context: Context) {
    var stationId = -1
    val resources = context.resources
    for (i in 1..7) {
        val curLine = resources.getStringArray(
            context.resources.getIdentifier(
                "stationsOnLine$i",
                "array",
                context.packageName
            )
        )

        curLine.forEach { stationName ->
            try {
                stationList.add(Station(++stationId, stationName, i))
                if (stationList.last().lineNum == stationList[stationList.lastIndex - 1].lineNum) {
                    metroGraph.addEdge(
                        stationList[stationList.lastIndex - 1].id,
                        stationList.last().id,
                    )
                    setAdjNodesLineNum(
                        Pair(
                            stationList[stationList.lastIndex - 1].id,
                            stationList.last().id
                        ), i
                    )
                    setAdjNodesLineNum(
                        Pair(
                            stationList.last().id,
                            stationList[stationList.lastIndex - 1].id,
                        ), i
                    )

                    setAdjNodesLineNum(Pair(stationList.last().id, stationList.last().id), i)
                }
            } catch (_: Exception) {
            }
        }
    }
    connectInterchangeStations(context)
    connectSideStations(context)
}

private fun connectInterchangeStations(context: Context) {
    context.resources.getStringArray(R.array.interchangeStations).drop(2).forEach {
        val foundedTwoSameNameStations = findStationObjectFromItsName(it)
        metroGraph.addEdge(foundedTwoSameNameStations[0].id, foundedTwoSameNameStations[1].id)

        setAdjNodesLineNum(
            Pair(
                foundedTwoSameNameStations[0].id,
                foundedTwoSameNameStations[1].id
            ), foundedTwoSameNameStations[1].lineNum
        )
        setAdjNodesLineNum(
            Pair(
                foundedTwoSameNameStations[1].id,
                foundedTwoSameNameStations[0].id
            ), foundedTwoSameNameStations[0].lineNum
        )
    }
}

fun findStationObjectFromItsName(stationName: String) =
    stationList.filter { it.name == stationName }

fun findStationObjectFromItsId(stationId: Int) = stationList.find { it.id == stationId }!!

fun setAdjNodesLineNum(edgePair: Pair<Int, Int>, edgeLineNum: Int) {
    GlobalObjects.adjNodesLineNum[edgePair] = edgeLineNum
}

fun log(stringMessage: String = "", wantToLogThis: Any?) =
    Log.d(TAG, "$stringMessage : $wantToLogThis")

fun connectSideStations(context: Context) {
    val resources = context.resources
    arrayOf(1, 4).forEach { lineNum ->
        val curLine = resources.getStringArray(
            resources.getIdentifier("sideStationsOfLine$lineNum", "array", context.packageName)
        )
        curLine.forEachIndexed { index, stationName ->
            stationList.add(Station(stationList.size, stationName, lineNum))
            val firstStationName = if (lineNum == 1) "شاهد - باقرشهر" else "بیمه"
            val firstStationId =
                if (index == 0) findStationObjectFromItsName(firstStationName)[0].id else stationList[stationList.lastIndex - 1].id
            try {
                metroGraph.addEdge(firstStationId, stationList.last().id)
                setAdjNodesLineNum(Pair(firstStationId, stationList.last().id), lineNum)
                setAdjNodesLineNum(Pair(stationList.last().id, firstStationId), lineNum)
            } catch (_: Exception) {
            }
        }
    }
}

@SuppressLint("MissingPermission")
fun getCurrentLocation(context: Context, onLocationChange: (Location) -> Unit) {
    log("got into currloc", true)
    val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        300000
    )
        .setWaitForAccurateLocation(true)
        .setMinUpdateIntervalMillis(1000)
        .setMaxUpdateDelayMillis(100)
        .build()

    val locationProvider = LocationServices.getFusedLocationProviderClient(context)
    val timeInMillis = measureTimeMillis {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.locations.lastOrNull() ?: return
                log("lat", UserLatitude)
                onLocationChange.invoke(Location(location.longitude, location.latitude))
            }
        }
        // Request location updates and listen for the callback.
        locationProvider.requestLocationUpdates(locationRequest, locationCallback, null)
    }
    log("time", timeInMillis)
}

@SuppressLint("ServiceCast")
fun isGpsEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

suspend fun setPairOfClosestStations(
    location: Location,
    onPairChange: (Triple<String, String, String>) -> Unit
) {
    val response =
        MetroYarNeshanApiService.retrofitService.findNearestStationsFromApi(
            latitude = location.y,
            longitude = location.x,
            term = "ایستگاه مترو"
        )
    onPairChange.invoke(
        Triple(
            response.items.first().title,
            response.items[1].title,
            response.items[2].title
        )
    )
    log("into set", response.count)
}

@Composable
fun Test2(context: Context) {
    var isLoading by remember { mutableStateOf(true) }
    var isLocEnabled by remember { mutableStateOf(false) }
    var location by remember { mutableStateOf(Location(0.0, 0.0)) }
    var triple by remember { mutableStateOf(Triple("", "", "")) }
    PermissionScreen(
        onPermissionGranted = {
            if (!isGpsEnabled(context))
                EnableLocationDialog {
                    isLocEnabled = true
                    log("enabled got 2", true)
                }
            else
                isLocEnabled = true
        }
    )
    // LaunchedEffect(key1 = isLocEnabled) {
    if (isLocEnabled) {
        CircularProgressBar(visible = isLoading)
        getCurrentLocation(context, onLocationChange = { location = it })
        log("isLocEnabled", location)
    }
    // }

    LaunchedEffect(key1 = location) {
        if (location?.x != 0.0) {
            setPairOfClosestStations(location = location, onPairChange = { triple = it })
            log("x is not 0", location)
        }
    }

    if (triple.first != "") {
        isLoading = false
        SuggestionStationsDialog(pair = findMatchingNames(triple))
    }

}

fun findMatchingNames(triple: Triple<String, String, String>): Pair<String, String> {
    val matchingNames = mutableSetOf<String>()

    // Check for a match with triple.first
    val firstName =
        stationList.map { it.name }.find { it.contains(triple.first, ignoreCase = true) }
    if (firstName != null) {
        matchingNames.add(firstName)
    }

    // Check for a match with triple.second
    val secondName =
        stationList.map { it.name }.find { it.contains(triple.second, ignoreCase = true) }
    if (secondName != null) {
        matchingNames.add(secondName)
    }

    // Check for a match with triple.third
    val thirdName =
        stationList.map { it.name }.find { it.contains(triple.third, ignoreCase = true) }
    if (thirdName != null) {
        matchingNames.add(thirdName)
    }

    return Pair(matchingNames.toList()[0], matchingNames.toList()[1])
}