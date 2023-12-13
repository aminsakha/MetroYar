package com.metroyar.model

import com.metroyar.R

enum class BottomNavItem(val icon: Int, val title: String) {
    Account(icon = R.drawable.baseline_feed_24, title = "اطلاعات"),
    StationsOnMap(icon = R.drawable.main, title = "نمایش ایستگاه ها روی نقشه شهر"),
    MetroMap(icon = R.drawable.baseline_map_24, title = "بروزترین نسخه نقشه مترو"),
    Navigation(icon = R.drawable.baseline_near_me_24, title = "مسیریابی بین دو ایستگاه مترو"),
}