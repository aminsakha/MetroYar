package com.metroyar

import com.metroyar.model.Station

object GlobalObjects {
    const val TAG = "testMetroYar"
    val adjNodesLineNum = mutableMapOf<Pair<Int, Int>, Int>()
    val stationList = mutableListOf<Station>()
    val metroGraph = MetroGraph(151)
     val tripleOfLinesAndTheirStartAndEndStations =
        mutableListOf<Triple<Int, String, String>>().apply {
            add(Triple(1, "تجریش", "کهریزک"))
            add(Triple(2, "فرهنگسرا", "تهران(صادقیه)"))
            add(Triple(3, "قائم", "آزدادگان"))
            add(Triple(4, "شهید کلاهدوز", "ارم سبز"))
            add(Triple(5, "تهران(صادقیه)", "شهید سلیمانی"))
            add(Triple(6, "شهید ستاری", "دولت آباد"))
            add(Triple(7, "میدان صنعت", "بسیج"))
        }

}