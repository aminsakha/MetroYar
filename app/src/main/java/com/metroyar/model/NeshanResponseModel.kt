package com.metroyar.model

import kotlinx.serialization.Serializable

@Serializable
data class NeshanSearchResponseModel(
    val count: Int,
    val items: List<SearchItem>
)

@Serializable
data class SearchItem(
    val title: String,
    val address: String="",
    val neighbourhood: String,
    val region: String,
    val type: String,
    val category: String,
    val location: Location
)

@Serializable
data class Location(
    val x: Double,
    val y: Double,
    val z: String=""
)