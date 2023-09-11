package com.metroyar.model

import java.time.LocalTime

data class TimeChunk(
    val start: LocalTime,
    val end: LocalTime,
    val frequency: Int  // in minutes
)