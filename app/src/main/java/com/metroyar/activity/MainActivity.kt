package com.metroyar.activity

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import com.metroyar.db.RealmObject
import com.metroyar.db.RealmObject.realmRepo
import com.metroyar.screen.NavGraphs
import com.metroyar.ui.theme.MetroYarTheme
import com.metroyar.utils.GlobalObjects
import com.metroyar.utils.initiateStationsAndAdjNodesLineNum
import com.metroyar.utils.log
import com.ramcosta.composedestinations.DestinationsNavHost
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Creating an extended library configuration.
        val config =
            YandexMetricaConfig.newConfigBuilder("c94e3b07-3e3f-499d-9100-f8db6012f0f5").build()
        // Initializing the AppMetrica SDK.
        YandexMetrica.activate(applicationContext, config)
        // Automatic tracking of user activity.
        YandexMetrica.enableActivityAutoTracking(applicationContext as Application)
        setContent {
            MetroYarTheme {
                realmRepo.initRealmObject(RealmObject.realm)
                initiateStationsAndAdjNodesLineNum(LocalContext.current)
                log("sattions", GlobalObjects.stationList)
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}
