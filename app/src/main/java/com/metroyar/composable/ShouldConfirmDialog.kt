package com.metroyar.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.metroyar.ui.theme.textColor

@Composable
fun ShouldConfirmAlertDialog(
    title: String,
    message: String,
    confirmBtnText: String,
    visible: Boolean = true,
    onDismissRequest: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    if (visible) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = {
                Text(
                    text = title, modifier = Modifier.fillMaxWidth(),color= textColor,
                    textAlign = TextAlign.End
                )
            },
            text = {
                Text(
                    text = message, modifier = Modifier.fillMaxWidth(),color= textColor,
                    textAlign = TextAlign.End
                )
            },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text(
                        text = confirmBtnText, modifier = Modifier.fillMaxWidth(),color= textColor,
                        textAlign = TextAlign.End
                    )
                }
            }
        )
    }
}
