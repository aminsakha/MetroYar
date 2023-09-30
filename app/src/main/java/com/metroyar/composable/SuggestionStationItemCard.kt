package com.metroyar.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.metroyar.ui.theme.textColor
import com.metroyar.ui.theme.zahrasBlack

@Composable
fun SuggestionStationItemCard(
    stationName: String,
    onSrcClicked: (String) -> Unit,
    onDstClicked: (String) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
        ),
        modifier = Modifier
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
    ) {
        val state = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(2.dp)
                .verticalScroll(state = state),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stationName, fontWeight = FontWeight.Bold,color= textColor,)
            Row {
                TextButton(
                    onClick = { onDstClicked.invoke(stationName) },
                ) {
                    Text("مقصد من باشه", color = zahrasBlack)
                }
                Spacer(modifier = Modifier.width(2.dp))
                TextButton(
                    onClick = { onSrcClicked.invoke(stationName) },
                ) {
                    Text("مبدا من باشه ", color = zahrasBlack)
                }
            }
        }
    }
}