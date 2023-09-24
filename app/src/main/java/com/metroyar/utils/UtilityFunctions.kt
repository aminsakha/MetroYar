package com.metroyar.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.net.*
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import com.airbnb.lottie.compose.LottieClipSpec
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationTokenSource
import com.metroyar.R
import com.metroyar.component_composable.*
import com.metroyar.model.Station
import com.metroyar.network.MetroYarNeshanApiService
import com.metroyar.component_composable.EnableLocationDialog
import com.metroyar.model.GPSCoordinate
import com.metroyar.model.line.LineFIve
import com.metroyar.model.line.LineFour
import com.metroyar.model.line.LineOne
import com.metroyar.model.line.LineSeven
import com.metroyar.model.line.LineSix
import com.metroyar.model.line.LineThree
import com.metroyar.model.line.LineTwo
import com.metroyar.screen.PermissionScreen
import com.metroyar.utils.GlobalObjects.TAG
import com.metroyar.utils.GlobalObjects.bestCurrentPath
import com.metroyar.utils.GlobalObjects.metroGraph
import com.metroyar.utils.GlobalObjects.stationList
import java.time.Duration
import java.time.LocalTime
import kotlin.math.roundToLong

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
                if (stationList.last().lineNumber == stationList[stationList.lastIndex - 1].lineNumber) {
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
            ), foundedTwoSameNameStations[1].lineNumber
        )
        setAdjNodesLineNum(
            Pair(
                foundedTwoSameNameStations[1].id,
                foundedTwoSameNameStations[0].id
            ), foundedTwoSameNameStations[0].lineNumber
        )
    }
}

fun findStationObjectFromItsName(stationName: String) =
    stationList.filter { it.stationName == stationName }

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
        } catch (e: Exception) {
        }
    }
}

fun isGpsEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

suspend fun findClosestStations(
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
    onDisMiss: (Boolean) -> Unit
) {
    var showSuggestionDialog by remember { mutableStateOf(true) }
    var shouldShowRash by remember { mutableStateOf(false) }
    var showInternetDialog by remember { mutableStateOf(true) }
    var clipSpec by remember { mutableStateOf(LottieClipSpec.Progress(0.0f, 0.31f)) }
    var isLocEnabled by remember { mutableStateOf(false) }
    var shouldShowPermission by remember { mutableStateOf(true) }
    var isWiFiEnabled by remember { mutableStateOf(false) }
    var location by remember { mutableStateOf(GPSCoordinate(0.0, 0.0)) }
    var closestStationsPair by remember { mutableStateOf(Pair("empty", "empty")) }

    UserClosestStationsDialog(
        onDismissRequest = {
            showSuggestionDialog = false
            onDisMiss.invoke(false)
        },
        pairOfClosestStations = convertNeshanStationNameToMyFormat(closestStationsPair),
        visible = showSuggestionDialog,
        srcOnclick = {
            showSuggestionDialog = false
            onSrcClicked.invoke(it)
        },
        dstOnClicked = {
            showSuggestionDialog = false
            onDstClicked.invoke(it)
        },
        clipSpec = clipSpec
    )

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
        title = stringResource(R.string.enableLoc),
        bodyMessage = if (!shouldShowRash) stringResource(R.string.enableLocMessage) else stringResource(
            R.string.enableLocMessageRash
        ),
        confirmBtnText = stringResource(R.string.confirmBtn),
        shouldShowRational = { shouldShowRash = it },
        onPermissionGranted = {
            shouldShowPermission = false
            if (!isGpsEnabled(context))
                EnableLocationDialog {
                    isLocEnabled = it
                    if (!isLocEnabled)
                        onDisMiss.invoke(false)
                }
            else
                isLocEnabled = true
        }
    )
    LaunchedEffect(key1 = isWiFiEnabled) {
        checkInternetConnection(context, onStatChange = { isWiFiEnabled = it })
    }

    if (!isWiFiEnabled && location.x == 0.0) {
        ShouldConfirmAlertDialog(
            visible = showInternetDialog,
            onConfirm = { showInternetDialog = false },
            onDismissRequest = {
                showInternetDialog = false
            },
            title = stringResource(R.string.netLostTitle),
            message = stringResource(R.string.internetLostMessage),
            confirmBtnText = stringResource(R.string.confirmBtn)
        )
    }
    if (isWiFiEnabled && isLocEnabled && location.x == 0.0) {
        GetCurrentLocation(context, onLocationChange = { location = it })
    }

    LaunchedEffect(key1 = location) {
        if (location.x != 0.0)
            findClosestStations(location = location, onPairChange = { closestStationsPair = it })
    }

    if (closestStationsPair.first != "empty") {
        log("my pair", closestStationsPair)
        clipSpec = LottieClipSpec.Progress(0.0f, 1f)
    }
}

fun convertNeshanStationNameToMyFormat(pair: Pair<String, String>): Pair<String, String> {
    val matchingNames = stationList.map { it.stationName }
        .filter {
            pair.first.contains(it) || pair.second.contains(it)
        }

    return Pair(matchingNames.getOrNull(0) ?: "", matchingNames.getOrNull(1) ?: "")
}

fun checkInternetConnection(context: Context, onStatChange: (Boolean) -> Unit) {
    val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()
    val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            log("network stat", "okeye")
            onStatChange.invoke(true)
            super.onAvailable(network)
        }

        override fun onLost(network: Network) {
            log("network stat", "ok nist")
            onStatChange.invoke(false)
            super.onLost(network)
        }
    }
    val connectivityManager =
        context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
    if (connectivityManager.activeNetwork == null)
        onStatChange.invoke(false)
    else
        onStatChange.invoke(true)
    connectivityManager.requestNetwork(networkRequest, networkCallback)
}

fun vibratePhone(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager =
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        val vibrator = vibratorManager.defaultVibrator
        vibrator.vibrate(
            VibrationEffect.createOneShot(
                20,
                VibrationEffect.EFFECT_TICK
            )
        )
    } else {
        val vibrator =
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        @Suppress("DEPRECATION")
        vibrator.vibrate(20)
    }
}

fun getNextTrain(lineNumber: Int, currentTime: LocalTime): String? {
    val metroLines =
        listOf(LineOne(), LineTwo(), LineThree(), LineFour(), LineFIve(), LineSix(), LineSeven())
    val metroLine = metroLines.find { it.number == lineNumber }

    metroLine?.let {
        for (timeChunk in it.timeTable) {
            if (currentTime.isAfter(timeChunk.start) && currentTime.isBefore(timeChunk.end)) {
                val minutesSinceChunkStart =
                    Duration.between(timeChunk.start, currentTime).toMinutes()
                val nextTrainIn =
                    timeChunk.frequency.toDouble() - (minutesSinceChunkStart % timeChunk.frequency.toDouble())

                return currentTime.plusMinutes(nextTrainIn.roundToLong()).toStringWithCustomFormat()
            }
        }
    }
    return LocalTime.of(
        5,
        30
    ).toStringWithCustomFormat()
}

fun LocalTime.toStringWithCustomFormat() = "$minute : $hour"
fun LocalTime.toMinutes(): Int {
    return hour * 60 + minute
}

fun minuteToLocalTime(): LocalTime {
    val hours = bestCurrentPath!!.wholePathTime.toInt() / 60
    val minutesLeft = bestCurrentPath!!.wholePathTime.toInt() % 60
    return LocalTime.of(hours, minutesLeft)
}
