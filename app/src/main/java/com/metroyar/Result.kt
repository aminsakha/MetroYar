package com.metroyar

import android.content.Context
import android.util.Log
import com.metroyar.GlobalObjects.graph
import com.metroyar.model.Path
import com.metroyar.utils.findStationObjectFromItsId
import com.metroyar.utils.findStationObjectFromItsName
import java.util.PriorityQueue

class Result(
    context: Context,
    private var startStationName: String,
    private var destStationName: String
) {
    private val intersectionNames =
        context.resources.getStringArray(R.array.interchangeStations).toMutableList()

    fun generatePossiblePaths(): PriorityQueue<Path> {
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
                        graph.findPath(
                            findStationObjectFromItsName(startStationName)[i].id,
                            findStationObjectFromItsName(destStationName)[0].id
                        ).map { findStationObjectFromItsId(it)[0] }.toMutableList()
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
                        graph.findPath(
                            findStationObjectFromItsName(startStationName)[0].id,
                            findStationObjectFromItsName(destStationName)[i].id
                        ).map { findStationObjectFromItsId(it)[0] }.toMutableList()
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
                            graph.findPath(
                                findStationObjectFromItsName(startStationName)[i].id,
                                findStationObjectFromItsName(destStationName)[j].id
                            ).map { findStationObjectFromItsId(it)[0] }.toMutableList()
                        )
                    )
                }
            }
        } else
            possiblePathsQueue.add(
                Path(
                    graph.findPath(
                        findStationObjectFromItsName(startStationName)[0].id,
                        findStationObjectFromItsName(destStationName)[0].id
                    ).map { findStationObjectFromItsId(it)[0] }.toMutableList()
                )
            )

        return possiblePathsQueue
    }
}