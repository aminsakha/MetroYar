package com.metroyar.utils

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.metroyar.utils.GlobalObjects.TAG
import com.metroyar.utils.GlobalObjects.metroGraph
import com.metroyar.utils.GlobalObjects.stationList
import com.metroyar.R
import com.metroyar.model.Station
import java.time.format.TextStyle

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
