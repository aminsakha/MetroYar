package com.metroyar.utils

import android.content.Context
import android.util.Log
import com.metroyar.utils.GlobalObjects.TAG
import com.metroyar.utils.GlobalObjects.metroGraph
import com.metroyar.utils.GlobalObjects.stationList
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
    var stationId = stationList.lastIndex
    val resources = context.resources
    for (i in 1..4 step 3) {
        val curLine = resources.getStringArray(
            context.resources.getIdentifier(
                "sideStationsOfLine$i",
                "array",
                context.packageName
            )
        )

        curLine.forEachIndexed { index, stationName ->
            try {
                stationList.add(Station(++stationId, stationName, i))
                when (i) {
                    1 -> {
                        if (index == 0) {
                            metroGraph.addEdge(
                                findStationObjectFromItsName("شاهد - باقرشهر")[0].id,
                                stationList.last().id,
                            )
                            setAdjNodesLineNum(
                                Pair(
                                    findStationObjectFromItsName("شاهد - باقرشهر")[0].id,
                                    stationList.last().id
                                ), i
                            )
                            setAdjNodesLineNum(
                                Pair(
                                    stationList.last().id,
                                    findStationObjectFromItsName("شاهد - باقرشهر")[0].id,
                                ), i
                            )
                        } else {
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
                        }
                    }

                    4 -> {
                        if (index == 0) {
                            metroGraph.addEdge(
                                findStationObjectFromItsName("بیمه")[0].id,
                                stationList.last().id,
                            )
                            setAdjNodesLineNum(
                                Pair(
                                    findStationObjectFromItsName("بیمه")[0].id,
                                    stationList.last().id
                                ), i
                            )
                            setAdjNodesLineNum(
                                Pair(
                                    stationList.last().id,
                                    findStationObjectFromItsName("بیمه")[0].id,
                                ), i
                            )
                        } else {
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
                        }
                    }
                }
                //setAdjNodesLineNum(Pair(stationList.last().id, stationList.last().id), i)
            } catch (_: Exception) {
            }
        }
    }
}
