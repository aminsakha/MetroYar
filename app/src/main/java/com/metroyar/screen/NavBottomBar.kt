package com.metroyar.screen

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getSystemService
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.metroyar.R
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RequiresApi(Build.VERSION_CODES.S)
@RootNavGraph(start = true)
@Destination
@Composable
fun NavigationBottom() {
    var selectedIndex by remember { mutableStateOf(1) }
    val navBarItems = remember { NavBarItems.values() }

    Scaffold(
        modifier = Modifier.padding(start = 8.dp, bottom = 8.dp, end = 8.dp),
        bottomBar = {
            AnimatedNavigationBar(
                selectedIndex = selectedIndex,
                modifier = Modifier.height(64.dp),
                cornerRadius = shapeCornerRadius(cornerRadius = 34.dp),
                ballAnimation = Parabolic(tween(300)),
                indentAnimation = Height(tween(600)),
                barColor = MaterialTheme.colorScheme.primary,
                ballColor = MaterialTheme.colorScheme.primary
            ) {
                val context = LocalContext.current
                navBarItems.forEach {
                    Box(
                        modifier = Modifier
                            .clickable {

                            }
                            .fillMaxSize()
                            .nonRipple {
                                selectedIndex = it.ordinal
                                val vibrator = context.getSystemService(VIBRATOR_SERVICE) as Vibrator
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    vibrator.vibrate(VibrationEffect.createOneShot(10, VibrationEffect.DEFAULT_AMPLITUDE))
                                } else {
                                    vibrator.vibrate(10)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(26.dp),
                            painter = painterResource(id = it.icon),
                            contentDescription = "",
                            tint = if (selectedIndex == it.ordinal) MaterialTheme.colorScheme.secondaryContainer
                            else MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }
            }
        }, content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                DeciderOfScreensInNavBotBar(input = selectedIndex)
            }
        }
    )
}

enum class NavBarItems(val icon: Int) {
    Account(icon = R.drawable.baseline_account_circle_24),
    Navigation(icon = R.drawable.baseline_near_me_24),
    MetroMap(icon = R.drawable.baseline_map_24)
}

fun Modifier.nonRipple(onclick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember {
            MutableInteractionSource()
        }) {
        onclick()
    }
}

@Composable
fun DeciderOfScreensInNavBotBar(input: Int) {
    when (input) {
        0 -> AccountScreen()
        1 -> NavigationScreen()
        2 -> MetroMapScreen()
    }
}