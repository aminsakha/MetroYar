package com.metroyar.model.line

import com.metroyar.model.TimeTable

abstract class Line {
    abstract val number: Int
    abstract val timeBetweenEveryAdjStation: Double
    abstract val timeTable: MutableList<TimeTable>
}