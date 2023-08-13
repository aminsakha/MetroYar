package com.metroyar.classes

import android.content.Context
import com.metroyar.R
import com.metroyar.utils.GlobalObjects.metroGraph
import com.metroyar.utils.GlobalObjects.tripleOfLinesAndTheirStartAndEndStations
import com.metroyar.model.Station
import com.metroyar.utils.findStationObjectFromItsId
import com.metroyar.utils.findStationObjectFromItsName
import com.metroyar.utils.getDirectionFromInterchangeStations
import com.metroyar.utils.log
import java.util.PriorityQueue


class Result(
    private val context: Context,
    private var startStationName: String,
    private var destStationName: String
) {
    private val intersectionNames =
        context.resources.getStringArray(R.array.interchangeStations).toMutableList()

    private fun generatePossiblePaths(): PriorityQueue<Path> {
        val possiblePathsQueue = PriorityQueue<Path> { pathA, pathB ->
            when {
                pathA.interchangesScore != pathB.interchangesScore -> pathA.interchangesScore - pathB.interchangesScore
                else -> pathA.stationsBetweenScore - pathB.stationsBetweenScore
            }
        }

        val startIsIntersection = intersectionNames.contains(startStationName)
        val destIsIntersection = intersectionNames.contains(destStationName)

        val startIndices = if (startIsIntersection) 0..1 else 0..0
        val destIndices = if (destIsIntersection) 0..1 else 0..0

        for (startIndex in startIndices) {
            for (destIndex in destIndices) {
                possiblePathsQueue.add(
                    Path(
                        metroGraph.findPath(
                            findStationObjectFromItsName(startStationName)[startIndex].id,
                            findStationObjectFromItsName(destStationName)[destIndex].id
                        ).map { findStationObjectFromItsId(it) }.toMutableList()
                    )
                )
            }
        }
        return possiblePathsQueue
    }

    fun convertPathToUserUnderstandableForm(): MutableList<String> {
        val path = generatePossiblePaths().peek()!!
        val stations = path.stationsOnPath.distinctBy { it.name }.toMutableList()
        log("path is :", path.stationsOnPath.map { it.name }.toSet().toMutableList())
        val pathStationNamesResult = mutableSetOf<String>().toMutableList()
        pathStationNamesResult.add("حرکت از ایستگاه ")
        pathStationNamesResult[pathStationNamesResult.lastIndex] =
            pathStationNamesResult.last() + stations[0].name
        pathStationNamesResult[pathStationNamesResult.lastIndex] =
            pathStationNamesResult.last() + " به سمت ${
                detectWhereToGoFromStartStation(
                    stations[0], stations[1]
                )
            }"

        stations.removeFirst()
        for (stationIndex in stations.indices) {
            try {
                pathStationNamesResult.add(stations[stationIndex].name)
                if (intersectionNames.contains(stations[stationIndex].name)) {
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
            if (curLine.contains(startStation.name) && curLine.contains(nextStation.name)) {
                return if (curLine.indexOf(startStation.name) < curLine.indexOf(nextStation.name))
                    tripleOfLinesAndTheirStartAndEndStations.find { it.first == i }!!.third
                else
                    tripleOfLinesAndTheirStartAndEndStations.find { it.first == i }!!.second
            }
        }
        return "wrong"
    }
}