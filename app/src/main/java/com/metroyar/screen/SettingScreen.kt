package com.metroyar.screen

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.metroyar.composable.SettingItem
import com.metroyar.db.PreferencesKeys
import com.metroyar.db.read
import com.metroyar.db.save
import com.metroyar.screen.destinations.NavigationBottomDestination
import com.metroyar.utils.dataStore
import com.metroyar.utils.log


import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun SettingScreen(navigator: DestinationsNavigator) {
    val context = LocalContext.current
    var playSoundStat by remember { mutableStateOf(true) }
    var vibrate by remember { mutableStateOf(true) }
    var bool by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = playSoundStat) {
        playSoundStat = read(context.dataStore, PreferencesKeys.SHOULD_PLAY_SOUND) ?: true
        vibrate = read(context.dataStore, PreferencesKeys.SHOULD_VIBRATE_PHONE) ?: true
        bool = true
        log("test", playSoundStat)
    }
    val coroutine = rememberCoroutineScope()
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            modifier = Modifier.shadow(8.dp),
            title = {
                Text(text = "تنظیمات")
            },
            navigationIcon = {
                IconButton(onClick = { navigator.navigate(NavigationBottomDestination()) }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Go back"
                    )
                }
            })
    },
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                log("vaghti zaher mishe", playSoundStat)
                if (bool)
                    SettingItem(
                        title = " پخش صدای جابجایی منو",
                        initialValue = playSoundStat,
                        onSwitchClicked = {
                            coroutine.launch {
                                save(
                                    context.dataStore,
                                    key = PreferencesKeys.SHOULD_PLAY_SOUND,
                                    it
                                )
                            }
                        })
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && bool)
                    SettingItem(
                        title = " ویبره بین صفحات منو",
                        initialValue = vibrate,
                        onSwitchClicked = {
                            coroutine.launch {
                                save(
                                    context.dataStore,
                                    key = PreferencesKeys.SHOULD_VIBRATE_PHONE,
                                    it
                                )
                            }
                        })
            }
        }
    )
}
