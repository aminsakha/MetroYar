package com.metroyar

import com.metroyar.model.Station

object GlobalObjects {
    val lines = HashMap<Pair<Int, Int>, Int>()
    val stationList = mutableListOf<Station>()
    val graph = Graph(146)
}