package com.metroyar

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.metroyar.GlobalObjects.metroGraph
import com.metroyar.model.Station
import com.metroyar.utils.findStationObjectFromItsId
import com.metroyar.utils.findStationObjectFromItsName
import com.metroyar.utils.log
import java.util.PriorityQueue


class Result(
    private val context: Context,
    private var startStationName: String,
    private var destStationName: String
) {
    private val tripleOfLinesAndTheirStartAndEndStations =
        mutableListOf<Triple<Int, String, String>>().apply {
            add(Triple(1, "تجریش", "کهریزک"))
            add(Triple(2, "فرهنگسرا", "تهران(صادقیه)"))
            add(Triple(3, "قایم", "آزدادگان"))
            add(Triple(4, "شهید کلاهدوز", "ارم سبز"))
            add(Triple(5, "تهران(صادقیه)", "شهید سلیمانی"))
            add(Triple(6, "شهید ستاری", "دولت آباد"))
            add(Triple(7, "میدان صنعت", "بسیج"))
        }
    private val intersectionNames =
        context.resources.getStringArray(R.array.interchangeStations).toMutableList()

    private fun generatePossiblePaths(): PriorityQueue<Path> {
        val possiblePathsQueue = PriorityQueue<Path> { pathA, pathB ->
            if (pathA.interchangesScore != pathB.interchangesScore) pathA.interchangesScore - pathB.interchangesScore
            else pathA.stationsBetweenScore - pathB.stationsBetweenScore
        }

        if ((intersectionNames.contains(startStationName) && !intersectionNames.contains(
                destStationName
            ))
        ) {
            for (i in 0..1) {
                possiblePathsQueue.add(
                    Path(
                        metroGraph.findPath(
                            findStationObjectFromItsName(startStationName)[i].id,
                            findStationObjectFromItsName(destStationName)[0].id
                        ).map { findStationObjectFromItsId(it) }.toMutableList()
                    )
                )
            }
        } else if (!intersectionNames.contains(startStationName) && intersectionNames.contains(
                destStationName
            )
        ) {
            for (i in 0..1) {
                possiblePathsQueue.add(
                    Path(
                        metroGraph.findPath(
                            findStationObjectFromItsName(startStationName)[0].id,
                            findStationObjectFromItsName(destStationName)[i].id
                        ).map { findStationObjectFromItsId(it) }.toMutableList()
                    )
                )
            }
        } else if (intersectionNames.contains(startStationName) && intersectionNames.contains(
                destStationName
            )
        ) {
            for (i in 0..1) {
                for (j in 0..1) {
                    possiblePathsQueue.add(
                        Path(
                            metroGraph.findPath(
                                findStationObjectFromItsName(startStationName)[i].id,
                                findStationObjectFromItsName(destStationName)[j].id
                            ).map { findStationObjectFromItsId(it) }.toMutableList()
                        )
                    )
                }
            }
        } else
            possiblePathsQueue.add(
                Path(
                    metroGraph.findPath(
                        findStationObjectFromItsName(startStationName)[0].id,
                        findStationObjectFromItsName(destStationName)[0].id
                    ).map { findStationObjectFromItsId(it) }.toMutableList()
                )
            )

        return possiblePathsQueue
    }

    fun convertPathToUserUnderstandableForm(): MutableList<String> {
        val path = generatePossiblePaths().peek()!!
        val stations = path.stationsOnPath.distinctBy { it.name }.toMutableList()
        log("path is :", path.stationsOnPath.map { it.name }.toSet().toMutableList())
        val pathStationNamesResult = mutableListOf<String>()
        pathStationNamesResult.add("حرکت از ایستگاه ")
        pathStationNamesResult.add(stations[0].name)
        pathStationNamesResult.add(" به سمت ")
        pathStationNamesResult.add(
            detectWhereToGoFromStartStation(
                stations[0],
                stations[1]
            )
        )
        stations.removeFirst()
        for (stationIndex in stations.indices) {
            try {
                if (intersectionNames.contains(stations[stationIndex].name)) {
                    pathStationNamesResult.add(stations[stationIndex].name)
                    pathStationNamesResult.add(
                        getDirectionFromInterchangeStations(
                            stations[stationIndex].id,
                            stations[stationIndex + 1].id
                        )
                    )
                } else
                    pathStationNamesResult.add(stations[stationIndex].name)
            } catch (_: Exception) {
            }
        }
        return pathStationNamesResult
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