package com.metroyar.model.line

import com.metroyar.model.TimeTable
import java.time.LocalTime

class LineThree : Line() {
    override val timeBetweenEveryAdjStation = 2.5
    override val number=3
    override val timeTable = mutableListOf(
        TimeTable(
            start = LocalTime.of(5, 30),
            end = LocalTime.of(7, 20),
            frequency = 10.5
        ),
        TimeTable(
            start = LocalTime.of(7, 20),
            end = LocalTime.of(15, 0),
            frequency = 7.5
        ),
        TimeTable(
            start = LocalTime.of(15, 0),
            end = LocalTime.of(22, 0),
            frequency = 8
        ),
    )
}