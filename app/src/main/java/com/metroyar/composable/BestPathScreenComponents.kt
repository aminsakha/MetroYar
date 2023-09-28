package com.metroyar.composable

import android.content.Context
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metroyar.R
import com.metroyar.classes.BestPathResult
import com.metroyar.classes.GuidPathStyle
import com.metroyar.screen.destinations.PathResultScreenDestination
import com.metroyar.screen.getLineColor
import com.metroyar.ui.theme.hint
import com.metroyar.ui.theme.line
import com.metroyar.ui.theme.textColor
import com.metroyar.utils.GlobalObjects
import com.metroyar.utils.GlobalObjects.deviceHeightInDp
import com.metroyar.utils.GlobalObjects.deviceWidthInDp
import com.metroyar.utils.GlobalObjects.readableFormResultList
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SrcAndDstCard(context: Context, navigator: DestinationsNavigator, src: String, dst: String) {
    OutlinedCard(
        onClick = {},
        enabled = false,
        modifier = Modifier.wrapContentSize()
    ) {
        Row(
            modifier = Modifier
                .padding(4.dp)
            ,horizontalArrangement = Arrangement.End,
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(modifier = Modifier.padding(vertical = 6.dp), onClick = {
                    readableFormResultList = BestPathResult(
                        context,
                        dst,
                        src
                    ).convertPathToReadableForm()
                    navigator.popBackStack()
                    navigator.navigate(
                        PathResultScreenDestination(
                            dst, src
                        )
                    )
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_multiple_stop_24),
                        contentDescription = "",
                        tint = textColor,
                        modifier = Modifier
                            .rotate(90f)
                            .size(26.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = src,
                    color = textColor,
                    textAlign = TextAlign.End
                )
                Spacer(modifier = Modifier.height(10.dp))
                Divider(
                    color = line,
                    modifier = Modifier.fillMaxWidth(0.6f),
                    thickness = 0.9.dp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = dst,
                    textAlign = TextAlign.End,
                    color = textColor
                )
            }
            Spacer(modifier = Modifier.width(18.dp))
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.Bottom) {
                Icon(
                    painter = painterResource(id = R.drawable.start_station),
                    contentDescription = "",
                    tint = getLineColor(src),
                    modifier = Modifier.padding(end = 4.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.three_dot),
                    contentDescription = "",
                    modifier = Modifier.padding(end = 4.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.distance),
                    contentDescription = "",
                    tint = getLineColor(dst),
                    modifier = Modifier.padding(end = 4.dp)
                )
            }

            // Spacer(modifier = Modifier.width(16.dp))

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArrivalsTime(pathTime: String, trainArrivalTime: String) {
    OutlinedCard(
        onClick = {},
        enabled = false,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = pathTime,
                    color = textColor,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Divider(
                color = line,
                modifier = Modifier.fillMaxWidth(0.8f),
                thickness = 0.9.dp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.padding(bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = trainArrivalTime,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = textColor
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandableCard(
    title: String,
    guidPathStyle: GuidPathStyle,
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f, label = ""
    )
    Card(
        colors = CardDefaults.outlinedCardColors(),
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 150,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = RoundedCornerShape(12.dp),
        onClick = {
            expandedState = !expandedState
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(2.5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(8f),
                    text = title,
                    fontSize = 14.sp,
                    textAlign = TextAlign.End,
                )
                IconButton(
                    modifier = Modifier
                        .weight(0.9f)
                        .alpha(if (title != guidPathStyle.guidPathStyleStringList.last()) 0.8f else 0f)
                        .rotate(rotationState),
                    onClick = {
                        expandedState = !expandedState
                    }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        tint = textColor,
                        contentDescription = "Drop-Down Arrow"
                    )
                }
            }
            if (expandedState) {
                LazyColumn(
                    modifier = Modifier
                        .padding(12.dp)
                        .heightIn(max = GlobalObjects.deviceHeightInDp / 3f),
                    horizontalAlignment = Alignment.End
                ) {
                    itemsIndexed(
                        guidPathStyle.mapOfGuidPathToItsChildren.getOrDefault(
                            title, emptyList()
                        ).toList()
                    ) { index, subStation ->
                        if (index == 0) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                color = hint,
                                text = "ایستگاه های عبوری در این مرحله",
                                fontSize = 12.sp,
                                textAlign = TextAlign.End,
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Divider(
                                color = line,
                                modifier = Modifier.fillMaxWidth(0.5f),
                                thickness = 0.9.dp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = subStation,
                            fontSize = 13.sp,
                            textAlign = TextAlign.End,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}