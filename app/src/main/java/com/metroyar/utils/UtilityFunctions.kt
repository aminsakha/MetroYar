package com.metroyar.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat.startActivity
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.metroyar.R
import com.metroyar.model.Station
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
fun getCurrentLocation(context: Context) {
    val locationProvider = LocationServices.getFusedLocationProviderClient(context)
    val locationRequest = LocationRequest()
    locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.locations.lastOrNull() ?: return
            log("res ", location.longitude)
            log("res ", location.latitude)
        }
    }

    // Request location updates and listen for the callback.
    locationProvider.requestLocationUpdates(locationRequest, locationCallback, null)

}