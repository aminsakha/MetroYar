package com.metroyar.model.line

import com.metroyar.model.TimeTable
import java.time.LocalTime

class LineTwo : Line() {
    override val number = 2
    override val timeTable = mutableListOf(
        TimeTable(
            start = LocalTime.of(5, 30),
            end = LocalTime.of(14, 50),
            frequency = 6
        ),
        TimeTable(
            start = LocalTime.of(14, 50),
            end = LocalTime.of(21, 30),
            frequency = 5.2
        ),
        TimeTable(
            start = LocalTime.of(21, 30),
            end = LocalTime.of(22, 0),
            frequency = 10.2
        ),
    )
}