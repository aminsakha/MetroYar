package com.metroyar.utils

import android.content.Context
import com.metroyar.R
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
                GlobalObjects.stationList.add(Station(++stationId, stationName, i))
                if (GlobalObjects.stationList.last().lineNumber == GlobalObjects.stationList[GlobalObjects.stationList.lastIndex - 1].lineNumber) {
                    GlobalObjects.metroGraph.addEdge(
                        GlobalObjects.stationList[GlobalObjects.stationList.lastIndex - 1].id,
                        GlobalObjects.stationList.last().id,
                    )
                    setAdjNodesLineNum(
                        Pair(
                            GlobalObjects.stationList[GlobalObjects.stationList.lastIndex - 1].id,
                            GlobalObjects.stationList.last().id
                        ), i
                    )
                    setAdjNodesLineNum(Pair(GlobalObjects.stationList.last().id, GlobalObjects.stationList.last().id), i)
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
        GlobalObjects.metroGraph.addEdge(foundedTwoSameNameStations[0].id, foundedTwoSameNameStations[1].id)

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

fun setAdjNodesLineNum(edgePair: Pair<Int, Int>, edgeLineNum: Int) {
    GlobalObjects.adjNodesLineNum[edgePair] = edgeLineNum
}

fun connectSideStations(context: Context) {
    val resources = context.resources
    arrayOf(1, 4).forEach { lineNum ->
        val curLine = resources.getStringArray(
            resources.getIdentifier("sideStationsOfLine$lineNum", "array", context.packageName)
        )
        curLine.forEachIndexed { index, stationName ->
            GlobalObjects.stationList.add(Station(GlobalObjects.stationList.size, stationName, lineNum))
            val firstStationName = if (lineNum == 1) "شاهد - باقرشهر" else "بیمه"
            val firstStationId =
                if (index == 0) findStationObjectFromItsName(firstStationName)[0].id else GlobalObjects.stationList[GlobalObjects.stationList.lastIndex - 1].id
            try {
                GlobalObjects.metroGraph.addEdge(firstStationId, GlobalObjects.stationList.last().id)
                setAdjNodesLineNum(Pair(firstStationId, GlobalObjects.stationList.last().id), lineNum)
                setAdjNodesLineNum(Pair(GlobalObjects.stationList.last().id, firstStationId), lineNum)
            } catch (_: Exception) {
            }
        }
    }
}