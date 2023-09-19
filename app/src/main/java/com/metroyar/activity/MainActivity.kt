package com.metroyar.activity

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.metroyar.db.RealmObject
import com.metroyar.db.RealmObject.realmRepo
import com.metroyar.screen.NavGraphs
import com.metroyar.ui.theme.MetroYarTheme
import com.metroyar.utils.GlobalObjects
import com.metroyar.utils.GlobalObjects.deviceHeightInDp
import com.metroyar.utils.GlobalObjects.deviceWidthInDp
import com.metroyar.utils.initiateStationsAndAdjNodesLineNum
import com.metroyar.utils.log
import com.ramcosta.composedestinations.DestinationsNavHost
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config =
            YandexMetricaConfig.newConfigBuilder("c94e3b07-3e3f-499d-9100-f8db6012f0f5").build()
        YandexMetrica.activate(applicationContext, config)
        YandexMetrica.enableActivityAutoTracking(applicationContext as Application)
        setContent {
            MetroYarTheme {
                val configuration = LocalConfiguration.current
                deviceWidthInDp = configuration.screenWidthDp.dp
                deviceHeightInDp = configuration.screenHeightDp.dp
                realmRepo.initRealmObject(RealmObject.realm)
                initiateStationsAndAdjNodesLineNum(LocalContext.current)
                log("sattions", GlobalObjects.stationList)
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}
