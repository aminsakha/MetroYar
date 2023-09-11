package com.metroyar.model.line

import com.metroyar.model.TimeTable
import java.time.LocalTime

class LineOne : Line() {
    override val number = 1
    override val timeTable = mutableListOf(
        TimeTable(
            start = LocalTime.of(5, 30),
            end = LocalTime.of(6, 30),
            frequency = 15
        ),
        TimeTable(
            start = LocalTime.of(6, 30),
            end = LocalTime.of(20, 10),
            frequency = 5
        ),
        TimeTable(
            start = LocalTime.of(20, 10),
            end = LocalTime.of(22, 0),
            frequency = 8
        ),
    )
}