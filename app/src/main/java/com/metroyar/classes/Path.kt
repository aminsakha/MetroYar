package com.metroyar.classes

import com.metroyar.model.Station

class Path(val stationsOnPath: MutableList<Station>) {
    var interchangesScore = 0
    var stationsBetweenScore = 0

    init {
        calculateInterchangesScore()
        calculateStationsBetweenScore()
    }

    private fun calculateInterchangesScore() {
        var currLine = -1
        stationsOnPath.forEach { currStation ->
            if (currStation.lineNum != currLine) {
                currLine = currStation.lineNum
                interchangesScore++
            }
        }
    }

    private fun calculateStationsBetweenScore() {
        stationsBetweenScore = stationsOnPath.size - 2
    }
}