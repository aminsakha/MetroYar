package com.metroyar.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable



@Serializable
data class NeshanSearchPlaceResponseModel(
     val count: Int,
     val items: List<FoundedPlace>
)