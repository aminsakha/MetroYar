package com.metroyar.model

import kotlinx.serialization.Serializable


@Serializable
data class GPSCoordinate(
     val x: Double = 0.0,
     val y: Double = 0.0,
     val z: String = ""
)