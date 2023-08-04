package com.metroyar.utils

import android.content.Context
import com.metroyar.GlobalObjects.graph
import com.metroyar.GlobalObjects.stationList
import com.metroyar.R
import com.metroyar.model.Station

fun initiateStations(context: Context) {
    var stationId = -1
    val resources = context.resources
    val packageName = context.packageName
    resources.getStringArray(R.array.stationsOnLine1)
    for (i in 1..7) {
        val curLineStations = resources.getStringArray(
            resources.getIdentifier(
                "stationsOnLine$i",
                "array",
                packageName
            )
        )
        curLineStations.forEach { stationName ->
            stationList.add(Station(++stationId, stationName, i))
            if (stationList.last().lineNum == stationList[stationList.lastIndex - 1].lineNum) {
                graph.addEdge(
                    stationList.last().id,
                    stationList[stationList.lastIndex - 1].id,
                )
                graph.addEdge(
                    stationList[stationList.lastIndex - 1].id,
                    stationList.last().id,
                )
            }
        }
    }
    connectDupIntersections(context)
}

private fun connectDupIntersections(context: Context) {
    TODO("Not yet implemented")
}
