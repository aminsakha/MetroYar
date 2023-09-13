package com.metroyar.model.line

import com.metroyar.model.TimeTable
import java.time.LocalTime

class LineOne : Line() {
    override val number = 1
    override val timeBetweenEveryAdjStation = 2.1
    override val timeTable = mutableListOf(
        TimeTable(
            start = LocalTime.of(5, 30),
            end = LocalTime.of(10, 0),
            frequency = 6.5
        ),
        TimeTable(
            start = LocalTime.of(10, 0),
            end = LocalTime.of(19, 30),
            frequency = 5
        ),
        TimeTable(
            start = LocalTime.of(19, 30),
            end = LocalTime.of(22, 0),
            frequency = 8
        ),
    )
}