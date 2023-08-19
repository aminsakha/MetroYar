package com.metroyar.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun OneBtnAlertDialog(
    title:String,
    message:String,
    okMessage:String,
    visible: Boolean,
    onDismissRequest: () -> Unit
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
                TextButton(onClick = onDismissRequest) {
                    Text(
                        text = okMessage, modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                }
            }
        )
    }
}
