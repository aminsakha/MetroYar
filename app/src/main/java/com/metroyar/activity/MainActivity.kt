package com.metroyar.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import com.metroyar.db.RealmObject
import com.metroyar.db.RealmObject.realmRepo
import com.metroyar.screen.NavGraphs
import com.metroyar.ui.theme.MetroYarTheme
import com.metroyar.utils.initiateStationsAndAdjNodesLineNum
import com.ramcosta.composedestinations.DestinationsNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MetroYarTheme {
                realmRepo.initRealmObject(RealmObject.realm)
                initiateStationsAndAdjNodesLineNum(LocalContext.current)
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}

