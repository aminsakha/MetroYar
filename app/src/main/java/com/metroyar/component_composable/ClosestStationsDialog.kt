package com.metroyar.component_composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun UserClosestStationsDialog(
    visible: Boolean = true,
    pair: Pair<String, String>,
    onDismissRequest: () -> Unit = {},
    srcOnclick: (String) -> Unit = {},
    dstOnClicked: (String) -> Unit = {}
) {
    if (visible)
        Dialog(onDismissRequest = { onDismissRequest() }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(410.dp)
                    .width(200.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                ),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    if (pair.first.isEmpty() && pair.second.isEmpty()) {
                        Text(text = "ایستگاه نزدیکی یافت نشد")
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { onDismissRequest() }) {
                            Text(text = "باشه")
                        }
                    }

                    if (pair.first.isNotEmpty())
                        Row {
                            SuggestionStationItemCard(
                                stationName = pair.first,
                                onDstClicked = dstOnClicked,
                                onSrcClicked = srcOnclick
                            )
                            Icon(imageVector = Icons.Filled.CheckCircle, contentDescription = "")
                        }

                    if (pair.second.isNotEmpty() && pair.second != pair.first)
                        SuggestionStationItemCard(
                            stationName = pair.second,
                            onDstClicked = dstOnClicked,
                            onSrcClicked = srcOnclick
                        )
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
        modifier = Modifier
            .background(Color.Blue)
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stationName, fontWeight = FontWeight.Bold)
            TextButton(
                onClick = { onSrcClicked.invoke(stationName) },
                modifier = Modifier.padding(4.dp),
            ) {
                Text("انتخاب به عنوان مبدا")
            }
            TextButton(
                onClick = { onDstClicked.invoke(stationName) },
                modifier = Modifier.padding(4.dp),
            ) {
                Text("انتخاب به عنوان مقصد")
            }
        }
    }
}