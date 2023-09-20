package com.metroyar.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metroyar.R
import com.metroyar.db.RealmObject
import com.metroyar.screen.destinations.NavigationBottomDestination
import com.metroyar.ui.theme.line
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

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SettingItem(title: String, initialValue: Boolean, onSwitchClicked: (Boolean) -> Unit) {

    Row(
        modifier = Modifier
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        ElevatedCard(
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .weight(2f),
            onClick = { },
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(10.dp)
            )
        }
        var checked by remember { mutableStateOf(initialValue) }
        val icon: (@Composable () -> Unit)? = if (checked) {
            {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                )
            }
        } else {
            {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                )
            }
        }
        //Spacer(modifier = Modifier.width(8.dp))
        Switch(
            modifier = Modifier
                .semantics { contentDescription = "" }
                .weight(1f),
            checked = checked,
            onCheckedChange = {
                checked = it
                onSwitchClicked.invoke(it)
            },
            thumbContent = icon
        )
    }
    Divider(
        color = line,
        modifier = Modifier.padding(horizontal = 26.dp),
        thickness = 0.18.dp
    )
}