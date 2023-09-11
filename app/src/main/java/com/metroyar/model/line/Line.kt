package com.metroyar.model.line

import com.metroyar.model.TimeChunk

abstract class Line {
    abstract val number: Int
    abstract val timeTable: MutableList<TimeChunk>
}