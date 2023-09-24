package com.metroyar.component_composable

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.metroyar.R
import com.metroyar.classes.UserFriendlyPathStyle
import com.metroyar.screen.getLineColor
import com.metroyar.ui.theme.line
import com.metroyar.ui.theme.redd
import com.metroyar.ui.theme.textColor
import com.metroyar.utils.GlobalObjects
import com.metroyar.utils.log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SrcAndDstCard(src: String, dst: String) {
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
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center

            ) {
                Text(
                    text = src,
                    color = textColor,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    painter = painterResource(id = R.drawable.start_station),
                    contentDescription = "",
                    tint = getLineColor(src),
                    modifier = Modifier.padding(end = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Divider(
                color = line,
                modifier = Modifier.padding(horizontal = 32.dp),
                thickness = 0.9.dp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = dst,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End,
                    color = textColor
                )
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    painter = painterResource(id = R.drawable.distance),
                    contentDescription = "",
                    tint = getLineColor(dst),
                    modifier = Modifier.padding(end = 4.dp)
                )
            }
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
                modifier = Modifier.padding(horizontal = 32.dp),
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
    userFriendlyPathStyle: UserFriendlyPathStyle,
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
                    durationMillis = 250,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = RoundedCornerShape(8.dp),
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
                    textAlign = TextAlign.End,
                )
                IconButton(
                    modifier = Modifier
                        .weight(1f)
                        .alpha(if (title != userFriendlyPathStyle.result.last()) 0.8f else 0f)
                        .rotate(rotationState),
                    onClick = {
                        expandedState = !expandedState
                    }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
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
                    items(
                        userFriendlyPathStyle.expandableItems.getOrDefault(
                            title, emptyList()
                        ).toList()
                    ) { subStation ->
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = subStation,
                            textAlign = TextAlign.End,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}