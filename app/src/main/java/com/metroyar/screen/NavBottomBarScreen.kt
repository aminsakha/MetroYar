package com.metroyar.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.metroyar.R
import com.metroyar.db.RealmObject.realmRepo
import com.metroyar.ui.theme.turnedOff
import com.metroyar.utils.GlobalObjects.lastMenuItemIndex
import com.metroyar.utils.GlobalObjects.stack
import com.metroyar.utils.log
import com.metroyar.utils.playSound
import com.metroyar.utils.vibratePhone
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun NavigationBottom(navigator: DestinationsNavigator) {
    val context = LocalContext.current
    var selectedMenuIndex by remember { mutableIntStateOf(lastMenuItemIndex) }
    var test0 by remember { mutableStateOf(lastMenuItemIndex == 0) }
    var test1 by remember { mutableStateOf(lastMenuItemIndex == 1) }
    var test2 by remember { mutableStateOf(lastMenuItemIndex == 2) }
    var selectedScreenTopBarTitle by remember { mutableStateOf(BottomNavItem.Navigation.title) }
    val bottomBarItems = remember { BottomNavItem.values() }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.onPrimary,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .shadow(8.dp)
                    .background(Color.White),
                title = { Text(text = selectedScreenTopBarTitle) })
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(MaterialTheme.colorScheme.onPrimary)
            ) {
                log("again", lastMenuItemIndex)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.03f),  // Adjust opacity as needed
                                ),
                                startY = 0.5f  // Adjust position as needed
                            )
                        )
                ) {
                    AnimatedNavigationBar(
                        selectedIndex = selectedMenuIndex,
                        modifier = Modifier.padding(start = 12.dp, bottom = 12.dp, end = 12.dp),
                        cornerRadius = shapeCornerRadius(cornerRadius = 34.dp),
                        ballAnimation = Parabolic(tween(300)),
                        indentAnimation = Height(tween(600)),
                        barColor = MaterialTheme.colorScheme.onSecondary,
                        ballColor = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        bottomBarItems.forEach {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .nonRipple {
                                        if (stack.isNotEmpty())
                                            when (stack.pop()) {
                                                0 -> test0 = false
                                                1 -> test1 = false
                                                2 -> test2 = false
                                                else -> {}
                                            }
                                        if (realmRepo.getShouldPlaySound())
                                            playSound(
                                                context = context,
                                                soundResourceId = R.raw.sound
                                            )
                                        selectedMenuIndex = it.ordinal
                                        stack.push(selectedMenuIndex)
                                        when (selectedMenuIndex) {
                                            0 -> test0 = true
                                            1 -> test1 = true
                                            2 -> test2 = true
                                        }
                                        lastMenuItemIndex = selectedMenuIndex
                                        selectedScreenTopBarTitle = it.title
                                        if (realmRepo.getShouldVibrate())
                                            vibratePhone(context)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .size(26.dp),
                                    painter = painterResource(id = it.icon),
                                    contentDescription = "",
                                    tint = if (selectedMenuIndex == it.ordinal) MaterialTheme.colorScheme.secondaryContainer
                                    else turnedOff
                                )
                            }
                        }
                    }
                }
            }

        }, content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
            ) {
                log("test0", test0)
                log("test1", test1)
                AnimatedVisibility(
                    visible = test0,
                    enter = slideInHorizontally() + fadeIn(),
                    exit = ExitTransition.None
                ) {
                    InfoScreen(navigator)
                }
                AnimatedVisibility(
                    visible = test1,
                    enter = slideInHorizontally() + fadeIn(),
                    exit = ExitTransition.None
                ) {
                    NavigationScreen(context = LocalContext.current, navigator = navigator)
                }
                AnimatedVisibility(
                    visible = test2,
                    enter = slideInHorizontally() + fadeIn(),
                    exit = ExitTransition.None
                ) {
                    MetroMapScreen()
                }
            }
        }
    )
}

enum class BottomNavItem(val icon: Int, val title: String) {
    Account(icon = R.drawable.baseline_feed_24, title = "اطلاعات"),
    Navigation(icon = R.drawable.baseline_near_me_24, title = "مسیریابی"),
    MetroMap(icon = R.drawable.baseline_map_24, title = "بروزترین نسخه نقشه مترو")
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