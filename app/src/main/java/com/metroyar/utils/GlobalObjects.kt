package com.metroyar.utils

import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import com.metroyar.classes.MetroGraph
import com.metroyar.model.Location
import com.metroyar.model.Station
import kotlinx.coroutines.flow.MutableStateFlow

object GlobalObjects {
    const val TAG = "testMetroYar"
    val adjNodesLineNum = mutableMapOf<Pair<Int, Int>, Int>()
    val stationList = mutableListOf<Station>()
    val metroGraph = MetroGraph(155)
    var startStation = ""
    var destStation = ""
    var UserLongitude =0.0
    var UserLatitude =0.0
    var resultList = mutableStateOf(listOf<String>())
    val locationFlow = MutableStateFlow<Location?>(null)
    val pairOfClosestStationsFlow = MutableStateFlow<Pair<String,String>?>(null)
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