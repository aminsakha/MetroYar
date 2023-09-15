package com.metroyar.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.metroyar.R
import com.metroyar.component_composable.InfoCardItem
import com.metroyar.model.InfoItem
import com.metroyar.screen.destinations.AboutUsScreenDestination
import com.metroyar.screen.destinations.FavoriteStationsScreenDestination
import com.metroyar.utils.log
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun AccountScreen(navigator: DestinationsNavigator) {
    Column {
        InfoCardItem(
            infoItem = InfoItem(
                title = "ایستگاه های نشان شده من",
                icon = ImageVector.vectorResource(id = R.drawable.baseline_bookmark_24_filled),
                onClickedItem = {navigator.navigate(FavoriteStationsScreenDestination())})
        )
        InfoCardItem(
            infoItem = InfoItem(
                title = "تنظیمات",
                icon = Icons.Filled.Settings,
                onClickedItem = {})
        )
        InfoCardItem(
            infoItem = InfoItem(
                title = "قابلیت های مترویار",
                icon = ImageVector.vectorResource(id = R.drawable.baseline_dashboard_24),
                onClickedItem = {})
        )
        InfoCardItem(
            infoItem = InfoItem(
                title = "درباره ما",
                icon = Icons.Filled.Info,
                onClickedItem = {
                    log("clicked", true)
                    navigator.navigate(AboutUsScreenDestination())
                })
        )
        InfoCardItem(
            infoItem = InfoItem(
                title = "نسخه   1.0.0 ",
                icon = ImageVector.vectorResource(id = R.drawable.baseline_update_24),
                onClickedItem = {})
        )
    }
}
