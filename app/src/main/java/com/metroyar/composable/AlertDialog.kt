package com.metroyar.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun OneBtnAlertDialog(
    title: String,
    message: String,
    okMessage: String,
    visible: Boolean = true,
    onDismissRequest: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    if (visible) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = {
                Text(
                    text = title, modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            },
            text = {
                Text(
                    text = message, modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text(
                        text = okMessage, modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                }
            }
        )
    }
}

@Composable
fun DialogWithImage(
    onDismissRequest: () -> Unit={}, pairOfTwoStationNames: Pair<String, String>,
    onShouldBeStartStaionOnClick: () -> Unit, onShouldBeDestStaionOnClick: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        // Draw a rectangle shape with rounded corners inside the dialog
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize().weight(1F),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                StationSuggestionItem(
                    stationName = pairOfTwoStationNames.first,
                    onShouldBeDestStaionOnClick = onShouldBeDestStaionOnClick,
                    onShouldBeStartStaionOnClick = onShouldBeStartStaionOnClick
                )
                StationSuggestionItem(
                    stationName = pairOfTwoStationNames.second,
                    onShouldBeDestStaionOnClick = onShouldBeDestStaionOnClick,
                    onShouldBeStartStaionOnClick = onShouldBeStartStaionOnClick
                )
            }
        }
    }
}

@Composable
fun StationSuggestionItem(
    stationName: String,
    onShouldBeStartStaionOnClick: () -> Unit,
    onShouldBeDestStaionOnClick: () -> Unit
) {
    Card(shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = stationName)
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(
                onClick = { onShouldBeStartStaionOnClick() },
                modifier = Modifier.padding(8.dp),
            ) {
                Text("انتخاب به عنوان مبدا")
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(
                onClick = { onShouldBeDestStaionOnClick() },
                modifier = Modifier.padding(8.dp),
            ) {
                Text("انتخاب به عنوان مقصد")
            }
        }
    }
}