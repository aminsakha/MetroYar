package com.metroyar.model

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
) {
    override fun toString(): String {
        return "$title , ${location.x} , ${location.y}"
    }
}
