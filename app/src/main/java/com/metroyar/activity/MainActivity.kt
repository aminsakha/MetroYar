package com.metroyar.activity

import android.animation.ObjectAnimator
import android.app.Application
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.metroyar.NavGraphs
import com.metroyar.R
import com.metroyar.db.RealmObject
import com.metroyar.db.RealmObject.realmRepo


import com.metroyar.ui.theme.MetroYarTheme
import com.metroyar.utils.GlobalObjects.deviceHeightInDp
import com.metroyar.utils.GlobalObjects.deviceWidthInDp
import com.metroyar.utils.initiateStationsAndAdjNodesLineNum
import com.ramcosta.composedestinations.DestinationsNavHost
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config =
            YandexMetricaConfig.newConfigBuilder(getString(R.string.yandex_api_key)).build()
        YandexMetrica.activate(applicationContext, config)
        YandexMetrica.enableActivityAutoTracking(applicationContext as Application)
        installSplashScreen().apply {
            setOnExitAnimationListener { screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.6f,
                    0.0f
                )
                zoomX.interpolator = OvershootInterpolator()
                zoomX.duration = 600L
                zoomX.doOnEnd { screen.remove() }

                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.6f,
                    0.0f
                )
                zoomY.interpolator = OvershootInterpolator()
                zoomY.duration = 600L
                zoomY.doOnEnd { screen.remove() }

                zoomX.start()
                zoomY.start()
            }
        }

        setContent {
            MetroYarTheme {
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
