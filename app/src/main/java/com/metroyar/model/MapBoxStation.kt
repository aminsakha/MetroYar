package com.metroyar.model

data class MapBoxStation(val title: String, val x: Double, val y: Double, val lineNum: Int){
    override fun toString(): String {
        return "$title -> $x"
    }
}
