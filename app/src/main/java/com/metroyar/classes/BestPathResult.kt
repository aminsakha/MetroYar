package com.metroyar.classes

import android.content.Context
import com.metroyar.R
import com.metroyar.model.Station
import com.metroyar.utils.GlobalObjects.adjNodesLineNum
import com.metroyar.utils.GlobalObjects.bestCurrentPath
import com.metroyar.utils.GlobalObjects.metroGraph
import com.metroyar.utils.GlobalObjects.tripleOfLinesAndTheirStartAndEndStations
import com.metroyar.utils.findStationObjectFromItsId
import com.metroyar.utils.findStationObjectFromItsName
import com.metroyar.utils.getDirectionFromInterchangeStations
import com.metroyar.utils.log
import java.util.PriorityQueue


class BestPathResult(
    private val context: Context,
    private var startStationName: String,
    private var destStationName: String
) {
    private val intersectionNames =
        context.resources.getStringArray(R.array.interchangeStations).toMutableList()

    private fun generatePossiblePaths(): PriorityQueue<Path> {
        val possiblePathsQueueBasedOnInterchanges = PriorityQueue<Path> { pathA, pathB ->
            when {
                pathA.interchangesScore != pathB.interchangesScore -> pathA.interchangesScore - pathB.interchangesScore
                else -> pathA.stationsBetweenScore - pathB.stationsBetweenScore
            }
        }

        val possiblePathsQueueBasedOnStationsBetween = PriorityQueue<Path> { pathA, pathB ->
            when {
                pathA.stationsBetweenScore != pathB.stationsBetweenScore -> pathA.stationsBetweenScore - pathB.stationsBetweenScore
                else -> pathA.interchangesScore - pathB.interchangesScore
            }
        }

        val startIsIntersection = intersectionNames.contains(startStationName)
        val destIsIntersection = intersectionNames.contains(destStationName)

        val startIndices =
            if (startIsIntersection && (startStationName != "بیمه" && startStationName != "شاهد - باقرشهر")) 0..1 else 0..0
        val destIndices =
            if (destIsIntersection && (destStationName != "بیمه" && destStationName != "شاهد - باقرشهر")) 0..1 else 0..0

        for (startIndex in startIndices) {
            for (destIndex in destIndices) {
                possiblePathsQueueBasedOnInterchanges.add(
                    Path(
                        stationsOnPath = metroGraph.findPath(
                            basedOnInterchange = true, src =
                            findStationObjectFromItsName(startStationName)[startIndex].id, dst =
                            findStationObjectFromItsName(destStationName)[destIndex].id
                        ).map { findStationObjectFromItsId(it) }.toMutableList()
                    )
                )
                possiblePathsQueueBasedOnStationsBetween.add(
                    Path(
                        stationsOnPath = metroGraph.findPath(
                            basedOnInterchange = false, src =
                            findStationObjectFromItsName(startStationName)[startIndex].id, dst =
                            findStationObjectFromItsName(destStationName)[destIndex].id
                        ).map { findStationObjectFromItsId(it) }.toMutableList()
                    )
                )
            }
        }

        return if ((possiblePathsQueueBasedOnInterchanges.peek()!!.wholePathTime - possiblePathsQueueBasedOnStationsBetween.peek()!!.wholePathTime) > 8)
            possiblePathsQueueBasedOnStationsBetween
        else
            possiblePathsQueueBasedOnInterchanges

    }

    fun convertPathToReadableForm(): MutableList<String> {
        bestCurrentPath = generatePossiblePaths().peek()!!
        val stations =
            bestCurrentPath!!.stationsOnPath.distinctBy { it.stationName }.toMutableList()
        val pathStationNamesResult = mutableSetOf<String>().toMutableList()
        pathStationNamesResult.add(stations[0].stationName)
        pathStationNamesResult[pathStationNamesResult.lastIndex] =
            pathStationNamesResult.last() + " به سمت ${
                detectWhereToGoFromStartStation(
                    stations[0], stations[1]
                )
            }"

        stations.removeFirst()
        for (stationIndex in stations.indices) {
            try {
                pathStationNamesResult.add(stations[stationIndex].stationName)
                if (intersectionNames.contains(stations[stationIndex].stationName)) {
                    val plus = getDirectionFromInterchangeStations(
                        stations[stationIndex].id,
                        stations[stationIndex + 1].id
                    )
                    if (!pathStationNamesResult.any { it.contains(plus) }) {
                        pathStationNamesResult[pathStationNamesResult.lastIndex] =
                            pathStationNamesResult.last() + " " +
                                    getDirectionFromInterchangeStations(
                                        stations[stationIndex].id,
                                        stations[stationIndex + 1].id
                                    )
                    }
                }
            } catch (_: Exception) {
            }
        }
        return pathStationNamesResult.toMutableList()
    }

    private fun detectWhereToGoFromStartStation(
        startStation: Station,
        nextStation: Station
    ): String {
        val resources = context.resources
        for (i in 1..7) {
            val curLine = resources.getStringArray(
                context.resources.getIdentifier(
                    "stationsOnLine$i",
                    "array",
                    context.packageName
                )
            )
            if (curLine.contains(startStation.stationName) && curLine.contains(nextStation.stationName)) {
                return if (curLine.indexOf(startStation.stationName) < curLine.indexOf(nextStation.stationName))
                    tripleOfLinesAndTheirStartAndEndStations.find { it.first == i }!!.third
                else
                    tripleOfLinesAndTheirStartAndEndStations.find { it.first == i }!!.second
            }
        }

        val triple =
            mutableListOf<Triple<Int, String, String>>().apply {
                add(Triple(1, "شاهد - باقرشهر", "فرودگاه امام خمینی"))
                add(Triple(4, "بیمه", "پایانه ۴ و ۶ فرودگاه مهرآباد"))
            }
        return if (startStation.id < nextStation.id)
            triple.find {
                it.first == adjNodesLineNum[Pair(
                    startStation.id,
                    nextStation.id
                )] && (it.second == "شاهد - باقرشهر" || it.second == "بیمه")
            }!!.third
        else
            triple.find {
                it.first == adjNodesLineNum[Pair(
                    startStation.id,
                    nextStation.id
                )] && (it.second == "شاهد - باقرشهر" || it.second == "بیمه")
            }!!.second
    }
}