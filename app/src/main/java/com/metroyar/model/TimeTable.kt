package com.metroyar.model

import java.time.LocalTime

data class TimeTable(
    val start: LocalTime,
    val end: LocalTime,
    val frequency: Number  // in minutes
)