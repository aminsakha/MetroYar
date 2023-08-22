package com.metroyar.utils

import androidx.compose.runtime.mutableStateOf
import com.metroyar.classes.MetroGraph
import com.metroyar.model.Station

object GlobalObjects {
    const val TAG = "testMetroYar"
    val adjNodesLineNum = mutableMapOf<Pair<Int, Int>, Int>()
    val stationList = mutableListOf<Station>()
    val metroGraph = MetroGraph(155)
    var startStation = mutableStateOf("")
    var destStation = mutableStateOf("")
    var resultList = mutableStateOf(listOf<String>())
    val tripleOfLinesAndTheirStartAndEndStations =
        mutableListOf<Triple<Int, String, String>>().apply {
            add(Triple(1, "تجریش", "کهریزک"))
            add(Triple(2, "فرهنگسرا", "تهران (صادقیه)"))
            add(Triple(3, "قائم", "آزادگان"))
            add(Triple(4, "شهید کلاهدوز", "علامه جعفری"))
            add(Triple(5, "تهران (صادقیه)", "شهید سپهبد قاسم سلیمانی(هشتگرد)"))
            add(Triple(6, "شهید ستاری", "دولت آباد"))
            add(Triple(7, "شهید دادمان", "بسیج"))
        }
}