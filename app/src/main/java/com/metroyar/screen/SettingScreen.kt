package com.metroyar.screen

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.metroyar.composable.SettingItem
import com.metroyar.db.RealmObject
import com.metroyar.screen.destinations.NavigationBottomDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun SettingScreen(navigator: DestinationsNavigator) {
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
                SettingItem(
                    title = " پخش صدای جابجایی منو",
                    initialValue = RealmObject.realmRepo.getShouldPlaySound(),
                    onSwitchClicked = {
                        coroutine.launch {
                            RealmObject.realmRepo.changeShouldPlaySound(it)
                        }
                    })
                SettingItem(
                    title = " ویبره بین صفحات منو",
                    initialValue = RealmObject.realmRepo.getShouldVibrate(),
                    onSwitchClicked = {
                        coroutine.launch {
                            RealmObject.realmRepo.changeShouldVibrate(it)
                        }
                    })
            }
        }
    )
}
