package com.metroyar.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieClipSpec
import com.metroyar.R
import com.metroyar.ui.theme.textColor
import com.metroyar.utils.GlobalObjects.deviceHeightInDp

@Composable
fun UserClosestStationsDialog(
    visible: Boolean = true,
    pairOfClosestStations: Pair<String, String>,
    onDismissRequest: () -> Unit = {},
    srcOnclick: (String) -> Unit = {},
    dstOnClicked: (String) -> Unit = {},
    clipSpec: LottieClipSpec
) {
    var isAnimationFinished by remember {
        mutableStateOf(false)
    }
    var showAnime by remember {
        mutableStateOf(true)
    }
    if (visible)
        Dialog(onDismissRequest = { onDismissRequest() }) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onTertiary,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(deviceHeightInDp.times(0.4f)),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                ),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "ایستگاه های نزدیک من",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color= textColor,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    AnimatedVisibility(visible = showAnime) {
                        if (showAnime)
                            ShowLottieAnimation(
                                animationRawId = R.raw.station_loading_animation,
                                clipSpec = clipSpec,
                                speed = 0.5f,
                                onAnimationFinished = {
                                    isAnimationFinished = it
                                    showAnime = false
                                }
                            )
                    }
                    AnimatedVisibility(visible = isAnimationFinished) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (isAnimationFinished && pairOfClosestStations.first.isEmpty() && pairOfClosestStations.second.isEmpty()) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(text = "ایستگاه نزدیکی یافت نشد", fontSize = 16.sp,color= textColor)
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(onClick = { onDismissRequest() }) {
                                    Text(text = "باشه", color = textColor)
                                }
                            }

                            if (pairOfClosestStations.first.isNotEmpty())
                                SuggestionStationItemCard(
                                    stationName = pairOfClosestStations.first,
                                    onDstClicked = dstOnClicked,
                                    onSrcClicked = srcOnclick
                                )

                            if (pairOfClosestStations.second.isNotEmpty() && pairOfClosestStations.second != pairOfClosestStations.first)
                                SuggestionStationItemCard(
                                    stationName = pairOfClosestStations.second,
                                    onDstClicked = dstOnClicked,
                                    onSrcClicked = srcOnclick
                                )
                        }
                    }
                }
            }
        }
}