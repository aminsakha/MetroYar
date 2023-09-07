package com.metroyar.model

import kotlinx.serialization.Serializable

@Serializable
data class NeshanSearchPlaceResponseModel(
    val count: Int,
    val items: List<FoundedPlace>
)

@Serializable
data class FoundedPlace(
    val title: String = "",
    val address: String = "",
    val neighbourhood: String = "",
    val region: String = "",
    val type: String = "",
    val category: String = "",
    val location: GPSCoordinate
)

@Serializable
data class GPSCoordinate(
    val x: Double,
    val y: Double,
    val z: String = ""
)