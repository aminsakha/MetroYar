package com.metroyar.model.line

import com.metroyar.model.TimeTable
import java.time.LocalTime

class LineSix () : Line() {
    override val number=6
    override val timeTable = mutableListOf(
        TimeTable(
            start = LocalTime.of(5, 30),
            end = LocalTime.of(22, 0),
            frequency = 16
        ),
    )
}