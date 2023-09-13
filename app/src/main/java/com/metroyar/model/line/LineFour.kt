package com.metroyar.model.line

import com.metroyar.model.TimeTable
import java.time.LocalTime

class LineFour : Line() {
    override val number=4
    override val timeTable = mutableListOf(
        TimeTable(
            start = LocalTime.of(5, 30),
            end = LocalTime.of(19, 15),
            frequency = 5
        ),
        TimeTable(
            start = LocalTime.of(19, 15),
            end = LocalTime.of(22,0),
            frequency = 7.5
        ),
    )
}