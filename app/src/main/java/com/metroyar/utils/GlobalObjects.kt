package com.metroyar.utils

import androidx.compose.ui.unit.Dp
import com.metroyar.classes.MetroGraph
import com.metroyar.classes.Path
import com.metroyar.model.Station
import java.util.Stack

object GlobalObjects {
    val stationList = mutableListOf<Station>()
    var bestCurrentPath: Path? = null
    var deviceWidthInDp = Dp(30f)
    var deviceHeightInDp = Dp(30f)
    val stack: Stack<Int> = Stack<Int>().apply { push(1) }
    const val TAG = "testMetroYar"
    val adjNodesLineNum = mutableMapOf<Pair<Int, Int>, Int>()
    val metroGraph = MetroGraph(350)
    var startStation = ""
    var destStation = ""
    var lastMenuItemIndex = 1
    var readableFormResultList = listOf<String>()
    val tripleOfLinesAndTheirStartAndEndStations =
        mutableListOf<Triple<Int, String, String>>().apply {
            add(Triple(1, "تجریش", "کهریزک"))
            add(Triple(2, "فرهنگسرا", "تهران (صادقیه)"))
            add(Triple(3, "قائم", "آزادگان"))
            add(Triple(4, "شهید کلاهدوز", "علامه جعفری"))
            add(Triple(5, "تهران (صادقیه)", "شهید قاسم سلیمانی"))
            add(Triple(6, "شهید ستاری", "دولت آباد"))
            add(Triple(7, "میدان کتاب", "بسیج"))
        }
}