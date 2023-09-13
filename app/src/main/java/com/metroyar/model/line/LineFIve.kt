package com.metroyar.model.line

import com.metroyar.model.TimeTable
import java.time.LocalTime

class LineFIve : Line() {
    override val number = 5
    override val timeTable = mutableListOf(
        TimeTable(
            start = LocalTime.of(5, 30),
            end = LocalTime.of(22, 0),
            frequency = 12
        ),
    )
}