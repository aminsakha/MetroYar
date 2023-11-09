package com.metroyar.classes

import com.metroyar.model.Station
import com.metroyar.model.line.*

class Path(val stationsOnPath: MutableList<Station>) {
    var interchangesScore = 0
    var stationsBetweenScore = 0
    var wholePathTime = 0.0

    init {
        calculateInterchangesScore()
        calculateStationsBetweenScore()
        calculateWholePathTime()
    }

    private fun calculateInterchangesScore() {
        var currLine = -1
        stationsOnPath.forEach { currStation ->
            if (currStation.lineNumber != currLine) {
                currLine = currStation.lineNumber
                interchangesScore++
            }
        }
    }

    private fun calculateWholePathTime() {
        val metroLines =
            listOf(
                LineOne(),
                LineTwo(),
                LineThree(),
                LineFour(),
                LineFIve(),
                LineSix(),
                LineSeven()
            )
        val list = stationsOnPath.reversed().distinctBy { it.stationName }.reversed()
        for (i in 0..list.size - 2)
            wholePathTime += metroLines.find { it.number == list[i].lineNumber }!!.timeBetweenEveryAdjStation
//bara bazo baste shodane dar
        wholePathTime += 0.2 * (stationsOnPath.size - 2)
        //baraye taviz khat
        wholePathTime += (interchangesScore - 1) * 6
    }

    private fun calculateStationsBetweenScore() {
        stationsBetweenScore = stationsOnPath.size - 2
    }
}