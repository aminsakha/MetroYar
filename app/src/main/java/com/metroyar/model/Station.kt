package com.metroyar.model

data class Station(
    var id: Int,
    var name: String,
    var lineNum: Int,
) {
    override fun toString(): String {
        return "$name : $lineNum"
    }
}