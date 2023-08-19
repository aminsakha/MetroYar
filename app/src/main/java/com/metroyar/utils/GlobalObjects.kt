package com.metroyar.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import com.metroyar.classes.MetroGraph
import com.metroyar.model.Station
import net.engawapg.lib.zoomable.ZoomState

object GlobalObjects {
    const val TAG = "testMetroYar"
    val adjNodesLineNum = mutableMapOf<Pair<Int, Int>, Int>()
    val stationList = mutableListOf<Station>()
    val metroGraph = MetroGraph(151)
    var startStation = mutableStateOf("")
    var destStation = mutableStateOf("")
    var resultList = mutableStateOf(listOf<String>())
    val tripleOfLinesAndTheirStartAndEndStations =
        mutableListOf<Triple<Int, String, String>>().apply {
            add(Triple(1, "تجریش", "کهریزک"))
            add(Triple(2, "فرهنگسرا", "تهران (صادقیه)"))
            add(Triple(3, "قائم", "آزادگان"))
            add(Triple(4, "شهید کلاهدوز", "ارم سبز"))
            add(Triple(5, "تهران (صادقیه)", "شهید سپهبد قاسم سلیمانی"))
            add(Triple(6, "شهید ستاری", "دولت آباد"))
            add(Triple(7, "میدان صنعت", "بسیج"))
        }
}