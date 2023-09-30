package com.metroyar.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable


@Serializable
data class FoundedPlace(
     val title: String = "",
     val address: String = "",
     val neighbourhood: String = "",
     val region: String = "",
     val type: String = "",
     val category: String = "",
     val location: GPSCoordinate = GPSCoordinate()
)