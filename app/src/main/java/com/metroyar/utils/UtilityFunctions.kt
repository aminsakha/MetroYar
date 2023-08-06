package com.metroyar.utils

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.metroyar.GlobalObjects
import com.metroyar.GlobalObjects.adjNodesLineNum
import com.metroyar.GlobalObjects.graph
import com.metroyar.GlobalObjects.stationList
import com.metroyar.R
import com.metroyar.Result
import com.metroyar.model.Station

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
                    graph.addEdge(
                        stationList[stationList.lastIndex - 1].id,
                        stationList.last().id,
                    )
                    graph.setAdjNodesLineNum(
                        Pair(
                            stationList[stationList.lastIndex - 1].id,
                            stationList.last().id
                        ), i
                    )
                    graph.setAdjNodesLineNum(Pair(stationList.last().id, stationList.last().id), i)
                }
            } catch (_: Exception) {
            }
        }
    }
    connectInterchangeStations(context)
}

private fun connectInterchangeStations(context: Context) {
    context.resources.getStringArray(R.array.interchangeStations).forEach {
        val foundedTwoSameNameStations = findStationObjectFromItsName(it)
        graph.addEdge(foundedTwoSameNameStations[0].id, foundedTwoSameNameStations[1].id)

        graph.setAdjNodesLineNum(
            Pair(
                foundedTwoSameNameStations[0].id,
                foundedTwoSameNameStations[1].id
            ), foundedTwoSameNameStations[1].lineNum
        )
        graph.setAdjNodesLineNum(
            Pair(
                foundedTwoSameNameStations[1].id,
                foundedTwoSameNameStations[0].id
            ), foundedTwoSameNameStations[0].lineNum
        )
    }
}

fun findStationObjectFromItsName(stationName: String) =
    stationList.filter { it.name == stationName }

fun findStationObjectFromItsId(stationId: Int) = stationList.filter { it.id == stationId }
