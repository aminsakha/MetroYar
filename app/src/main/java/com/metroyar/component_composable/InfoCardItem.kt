package com.metroyar.component_composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metroyar.model.InfoItem
import com.metroyar.ui.theme.line
import com.metroyar.ui.theme.textColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoCardItem(modifier: Modifier = Modifier, infoItem: InfoItem) {
        Row(
            modifier = modifier
                .fillMaxWidth().clickable { infoItem.onClickedItem() }
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            if (infoItem.endText.isNotEmpty())
                Text(text = infoItem.endText, modifier = Modifier.align(Alignment.CenterVertically), color = line)
            else
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            Text(
                text = infoItem.title,
                fontSize = 14.sp,
                color = textColor,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
            Spacer(modifier = Modifier.width(12.dp))

            Icon(
                imageVector = infoItem.icon,
                contentDescription = infoItem.title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

        }
    }