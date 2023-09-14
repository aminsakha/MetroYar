package com.metroyar.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.metroyar.R
import com.metroyar.component_composable.InfoCardItem
import com.metroyar.model.InfoItem

@Composable
fun AccountScreen() {
    Column {
        InfoCardItem(
            infoItem = InfoItem(
                title = "ایستگاه های نشان شده من",
                icon = ImageVector.vectorResource(id = R.drawable.baseline_bookmark_24_filled),
                onClickedScreen = {})
        )
        InfoCardItem(
            infoItem = InfoItem(
                title = "تنظیمات",
                icon = Icons.Filled.Settings,
                onClickedScreen = {})
        )
        InfoCardItem(
            infoItem = InfoItem(
                title = "قابلیت های مترویار",
                icon = ImageVector.vectorResource(id = R.drawable.baseline_dashboard_24),
                onClickedScreen = {})
        )
        InfoCardItem(
            infoItem = InfoItem(
                title = "درباره ما",
                icon = Icons.Filled.Info,
                onClickedScreen = {})
        )
        InfoCardItem(
            infoItem = InfoItem(
                title = "نسخه ",
                icon = ImageVector.vectorResource(id = R.drawable.baseline_update_24),
                onClickedScreen = {})
        )
    }
}
