package com.metroyar.utils

import android.content.Context
import com.metroyar.R
import com.metroyar.model.Station

fun initiateStationsAndAdjNodesLineNum(context: Context) {
    val lines = listOf(
        R.array.stationsOnLine1,
        R.array.stationsOnLine2,
        R.array.stationsOnLine3,
        R.array.stationsOnLine4,
        R.array.stationsOnLine5,
        R.array.stationsOnLine6,
        R.array.stationsOnLine7,
    )
    var stationId = -1
    val resources = context.resources
    lines.forEachIndexed { i, line ->
        val curLine = resources.getStringArray(line)

        curLine.forEach { stationName ->
            try {
                GlobalObjects.stationList.add(Station(++stationId, stationName, i+1))
                if (GlobalObjects.stationList.last().lineNumber == GlobalObjects.stationList[GlobalObjects.stationList.lastIndex - 1].lineNumber) {
                    GlobalObjects.metroGraph.addEdge(
                        GlobalObjects.stationList[GlobalObjects.stationList.lastIndex - 1].id,
                        GlobalObjects.stationList.last().id,
                    )
                    setAdjNodesLineNum(
                        Pair(
                            GlobalObjects.stationList[GlobalObjects.stationList.lastIndex - 1].id,
                            GlobalObjects.stationList.last().id
                        ), i+1
                    )
                    setAdjNodesLineNum(Pair(GlobalObjects.stationList.last().id, GlobalObjects.stationList.last().id), i+1)
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
    val lineResources = listOf(R.array.sideStationsOfLine1, R.array.sideStationsOfLine4)
    val lineNumbers = listOf(1, 4)

    lineResources.forEachIndexed { index, resourceId ->
        val lineNum = lineNumbers[index]
        val curLine = resources.getStringArray(resourceId)
        val firstStationName = if (lineNum == 1) "شاهد - باقرشهر" else "بیمه"

        curLine.forEachIndexed { curIndex, stationName ->
            GlobalObjects.stationList.add(Station(GlobalObjects.stationList.size, stationName, lineNum))
            val firstStationId = if (curIndex == 0) findStationObjectFromItsName(firstStationName)[0].id
            else GlobalObjects.stationList[GlobalObjects.stationList.lastIndex - 1].id
            try {
                GlobalObjects.metroGraph.addEdge(firstStationId, GlobalObjects.stationList.last().id)
                setAdjNodesLineNum(Pair(firstStationId, GlobalObjects.stationList.last().id), lineNum)
                setAdjNodesLineNum(Pair(GlobalObjects.stationList.last().id, firstStationId), lineNum)
            } catch (_: Exception) {
            }
        }
    }
}
