package com.metroyar.component_composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metroyar.R
import com.metroyar.db.RealmObject.realmRepo

@Composable
fun DropDownStationSuggestionItem(
    itemName: String,
    onItemSelected: (String) -> Unit,
    onStarSelected: (Boolean) -> Unit,
) {
    var isStarred by remember {
        mutableStateOf(false)
    }
    isStarred = realmRepo.getListOfFavoriteStations().contains(itemName)

    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onItemSelected(itemName) }
        .padding(10.dp)) {
        IconButton(onClick = {
            onStarSelected.invoke(isStarred)
            isStarred = !isStarred
        }) {
            Icon(
                imageVector = if (isStarred) ImageVector.vectorResource(id = R.drawable.baseline_bookmark_24_filled)
                else ImageVector.vectorResource(id = R.drawable.baseline_bookmark_border_24_out_lined),
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