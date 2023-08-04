package com.metroyar

import com.metroyar.model.Station

object GlobalObjects {
    const val TAG = "testMetroYar"
    val adjNodesLineNum = mutableMapOf <Pair<Int,Int>,Int>()
    val stationList = mutableListOf<Station>()
    val graph = Graph(151)
}