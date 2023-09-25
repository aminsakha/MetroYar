package com.metroyar.component_composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieClipSpec
import com.metroyar.R
import com.metroyar.ui.theme.textColor
import com.metroyar.ui.theme.turnedOff2
import com.metroyar.utils.GlobalObjects
import com.metroyar.utils.GlobalObjects.deviceHeightInDp
import com.metroyar.utils.GlobalObjects.deviceWidthInDp
import com.metroyar.utils.SuggestionStationsLayout
import com.metroyar.utils.log

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
                                Text(text = "ایستگاه نزدیکی یافت نشد", fontSize = 16.sp)
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

@Composable
fun SuggestionStationItemCard(
    stationName: String,
    onSrcClicked: (String) -> Unit,
    onDstClicked: (String) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
        ),
        modifier = Modifier
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
    ) {
        val state = rememberScrollState()
        Column(
            modifier = Modifier.padding(2.dp).verticalScroll(state = state),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stationName, fontWeight = FontWeight.Bold)
            Row {
                TextButton(
                    onClick = { onDstClicked.invoke(stationName) },
                ) {
                    Text("مقصد من باشه", color = turnedOff2)
                }
                 Spacer(modifier = Modifier.width(2.dp))
                TextButton(
                    onClick = { onSrcClicked.invoke(stationName) },
                ) {
                    Text("مبدا من باشه ",color = turnedOff2)
                }
            }
        }
    }
}
