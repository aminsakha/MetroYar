package com.metroyar.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable


@Serializable
data class GPSCoordinate(
     val x: Double = 0.0,
     val y: Double = 0.0,
     val z: String = ""
)