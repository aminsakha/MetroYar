package com.metroyar.component_composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metroyar.R
import com.metroyar.db.RealmObject
import com.metroyar.db.getItems

@Composable
fun DropDownStationSuggestionItem(
    itemName: String,
    onItemSelected: (String) -> Unit,
    onStarSelected: (Boolean) -> Unit,
) {
    var isStarred by remember {
        mutableStateOf(false)
    }
    isStarred = getItems(realm = RealmObject.realm).contains(
        itemName
    )

    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onItemSelected(itemName) }
        .padding(10.dp)) {
        IconButton(onClick = {
            onStarSelected.invoke(isStarred)
            isStarred = !isStarred
        }) {
            Icon(
                imageVector = if (isStarred) Icons.Filled.Star else ImageVector.vectorResource(id = R.drawable.baseline_star_outline_24),
                contentDescription = "Star",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 10.dp, end = 10.dp)
            )
        }
        Text(
            text = itemName,
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End
        )
    }
}