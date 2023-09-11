package com.metroyar.model.line

import com.metroyar.model.TimeTable
import java.time.LocalTime

class LineThree : Line() {
    override val number=3
    override val timeTable = mutableListOf(
        TimeTable(
            start = LocalTime.of(5, 30),
            end = LocalTime.of(19, 0),
            frequency = 8.5
        ),
        TimeTable(
            start = LocalTime.of(19, 0),
            end = LocalTime.of(22, 0),
            frequency = 9
        ),
    )
}