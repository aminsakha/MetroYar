package com.metroyar.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metroyar.ui.theme.line


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SettingItem(title: String, initialValue: Boolean, onSwitchClicked: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        var checked by remember { mutableStateOf(initialValue) }
        val icon: (@Composable () -> Unit) = if (checked) {
            {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                )
            }
        } else {
            {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                )
            }
        }
        Switch(
            modifier = Modifier
                .semantics { contentDescription = "" }
                .weight(1f),
            checked = checked,
            onCheckedChange = {
                checked = it
                onSwitchClicked.invoke(it)
            },
            thumbContent = icon
        )
        ElevatedCard(
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .weight(3f),
            onClick = { },
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(10.dp)
            )
        }
    }
    Divider(
        color = line,
        modifier = Modifier.padding(horizontal = 26.dp),
        thickness = 0.18.dp
    )
}