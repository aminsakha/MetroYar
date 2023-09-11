package com.metroyar.model.line

import com.metroyar.model.TimeTable

abstract class Line {
    abstract val number: Int
    abstract val timeTable: MutableList<TimeTable>
}