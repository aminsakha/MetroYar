package com.metroyar.activity

import android.app.Application
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.metroyar.R
import com.metroyar.db.RealmObject
import com.metroyar.db.RealmObject.realmRepo
import com.metroyar.screen.NavGraphs
import com.metroyar.ui.theme.MetroYarTheme
import com.metroyar.utils.GlobalObjects.deviceHeightInDp
import com.metroyar.utils.GlobalObjects.deviceWidthInDp
import com.metroyar.utils.GlobalObjects.stationList
import com.metroyar.utils.initiateStationsAndAdjNodesLineNum
import com.metroyar.utils.log
import com.ramcosta.composedestinations.DestinationsNavHost
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import kotlinx.coroutines.delay


class MainActivity : ComponentActivity() {
    private var keepSplashOpened = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config =
            YandexMetricaConfig.newConfigBuilder(getString(R.string.yandex_api_key)).build()
        YandexMetrica.activate(applicationContext, config)
        YandexMetrica.enableActivityAutoTracking(applicationContext as Application)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            installSplashScreen().setKeepOnScreenCondition { keepSplashOpened }
        else
            installSplashScreen()

        setContent {
            MetroYarTheme {
                LaunchedEffect(key1 = Unit) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        delay(800)
                        keepSplashOpened = false
                    }
                }
                val configuration = LocalConfiguration.current
                deviceWidthInDp = configuration.screenWidthDp.dp
                deviceHeightInDp = configuration.screenHeightDp.dp
                realmRepo.initRealmObject(RealmObject.realm)
                initiateStationsAndAdjNodesLineNum(LocalContext.current)
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}
