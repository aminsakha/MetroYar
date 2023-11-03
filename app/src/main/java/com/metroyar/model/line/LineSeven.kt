package com.metroyar.model.line

import com.metroyar.model.TimeTable
import java.time.LocalTime

class LineSeven : Line() {
    override val timeBetweenEveryAdjStation = 3.0
    override val number = 7
    override val timeTable = mutableListOf(
        TimeTable(
            start = LocalTime.of(5, 30),
            end = LocalTime.of(22, 0),
            frequency = 12
        ),
    )
}