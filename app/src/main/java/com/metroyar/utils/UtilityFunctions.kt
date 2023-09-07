package com.metroyar.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
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
import com.metroyar.composable.OneBtnAlertDialog
import com.metroyar.composable.SuggestionStationsDialog
import com.metroyar.model.Location
import com.metroyar.model.Station
import com.metroyar.network.MetroYarNeshanApiService
import com.metroyar.screen.EnableLocationDialog
import com.metroyar.screen.PermissionScreen
import com.metroyar.utils.GlobalObjects.TAG
import com.metroyar.utils.GlobalObjects.metroGraph
import com.metroyar.utils.GlobalObjects.stationList


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
    log("got into getCurrentLocation", true)
    val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        300000
    )
        .setWaitForAccurateLocation(true)
        .setMinUpdateIntervalMillis(1000)
        .setMaxUpdateDelayMillis(100)
        .build()

    val locationProvider = LocationServices.getFusedLocationProviderClient(context)
    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.locations.lastOrNull() ?: return
            onLocationChange.invoke(Location(location.longitude, location.latitude))
        }
    }
    // Request location updates and listen for the callback.
    locationProvider.requestLocationUpdates(locationRequest, locationCallback, null)
}

@SuppressLint("ServiceCast")
fun isGpsEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

suspend fun setPairOfClosestStations(
    location: Location,
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
        log("filter list", pair)
        onPairChange.invoke(pair)
    } catch (_: Exception) {
    }
}

@Composable
fun SuggestionStationsLayout(
    context: Context,
    onDstClicked: (String) -> Unit,
    onSrcClicked: (String) -> Unit,
    onSuggestionStationsDialogDisMiss: (Boolean) -> Unit,
    onDisMiss: (Boolean) -> Unit
) {
    var showSuggestionDialog by remember { mutableStateOf(true) }
    var showInternetDialog by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(true) }
    var isLocEnabled by remember { mutableStateOf(false) }
    var shouldShowPermission by remember { mutableStateOf(true) }
    var isWiFiEnabled by remember { mutableStateOf(false) }
    var location by remember { mutableStateOf(Location(0.0, 0.0)) }
    var pair by remember { mutableStateOf(Pair("", "")) }
    PermissionScreen(
        visible = shouldShowPermission,
        permissionList = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ),
        onDismissRequest = {
            shouldShowPermission = false
            onDisMiss.invoke(false)
        },
        title = "فعال کردن موقعیت مکانی",
        bodyMessage = "برای اینکه بهتون بگیم دقیقا چه ایستگاهایی نزدیکتونن باید این دسترسی رو داشته باشیم",
        confirmBtnText = "باشه",
        onPermissionGranted = {
            shouldShowPermission = false
            if (!isGpsEnabled(context))
                EnableLocationDialog {
                    isLocEnabled = it
                }
            else
                isLocEnabled = true
        }
    )
    LaunchedEffect(key1 = isWiFiEnabled) {
        checkInternetConnection(context, onStatChange = { isWiFiEnabled = it })
    }

    if (!isWiFiEnabled && location.x == 0.0) {
        OneBtnAlertDialog(
            visible = showInternetDialog,
            onConfirm = { showInternetDialog = false },
            onDismissRequest = {
                showInternetDialog = false
                onDisMiss.invoke(false)
            },
            title = "قطعی اینترنت",
            message = "اینترنت باید وصل باشه",
            confirmBtnText = "اوکیه"
        )
    }
    if (isWiFiEnabled && isLocEnabled && location.x == 0.0) {
        CircularProgressBar(visible = isLoading)
        getCurrentLocation(context, onLocationChange = { location = it })
    }

    LaunchedEffect(key1 = location) {
        if (location.x != 0.0)
            setPairOfClosestStations(location = location, onPairChange = { pair = it })
    }

    if (pair.first != "") {
        isLoading = false
        log("my pair", pair)
        SuggestionStationsDialog(
            onDismissRequest = {
                showSuggestionDialog = false
                onSuggestionStationsDialogDisMiss.invoke(false)
            },
            pair = findMatchingNames(pair),
            visible = showSuggestionDialog,
            srcOnclick = {
                showSuggestionDialog = false
                onSrcClicked.invoke(it)
            },
            dstOnClicked = {
                showSuggestionDialog = false
                onDstClicked.invoke(it)
            })
    }
}

fun findMatchingNames(pair: Pair<String, String>): Pair<String, String> {
    val matchingNames = mutableSetOf<String>()

    // Check for a match with triple.first
    val firstName =
        stationList.map { it.name }.find { pair.first.contains(it, ignoreCase = true) }
    if (firstName != null) {
        matchingNames.add(firstName)
    }

    val secondName =
        stationList.map { it.name }.find { pair.second.contains(it, ignoreCase = true) }
    if (secondName != null) {
        matchingNames.add(secondName)
    }

    return if (matchingNames.size > 1)
        Pair(
            matchingNames.toList().getOrNull(0) ?: "",
            matchingNames.toList().getOrNull(1) ?: ""
        )
    else
        Pair(
            matchingNames.toList().getOrNull(0) ?: "",
            ""
        )
}

fun checkInternetConnection(context: Context, onStatChange: (Boolean) -> Unit) {
    val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()
    val networkCallback = object : ConnectivityManager.NetworkCallback() {

        // network is available for use
        override fun onAvailable(network: Network) {
            log("network stat", "okeye")
            onStatChange.invoke(true)
            super.onAvailable(network)
        }

        // Network capabilities have changed for the network
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            log("network stat", "change")
        }

        // lost network connection
        override fun onLost(network: Network) {
            log("network stat", "ok nist")
            onStatChange.invoke(false)
            super.onLost(network)
        }
    }
    val connectivityManager =
        context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
    if (connectivityManager.activeNetwork == null) {
        onStatChange.invoke(false)
        log("sat", "null")
    } else
        onStatChange.invoke(true)
    connectivityManager.requestNetwork(networkRequest, networkCallback)
}