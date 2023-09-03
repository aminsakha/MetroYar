package com.metroyar.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
fun SuggestionStationsDialog(
    visible: Boolean=true,
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
                    SuggestionStationItem(
                        stationName = pair.first,
                        dstOnClicked = dstOnClicked,
                        srcOnclick = srcOnclick
                    )
                    SuggestionStationItem(
                        stationName = pair.second,
                        dstOnClicked = dstOnClicked,
                        srcOnclick = srcOnclick
                    )
                }
            }
        }
}

@Composable
fun SuggestionStationItem(stationName: String, srcOnclick: (String) -> Unit, dstOnClicked: (String) -> Unit) {
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
                onClick = { srcOnclick.invoke(stationName) },
                modifier = Modifier.padding(4.dp),
            ) {
                Text("انتخاب به عنوان مبدا")
            }
            TextButton(
                onClick = { dstOnClicked.invoke(stationName) },
                modifier = Modifier.padding(4.dp),
            ) {
                Text("انتخاب به عنوان مقصد")
            }
        }
    }
}