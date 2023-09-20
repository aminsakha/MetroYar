package com.metroyar.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.metroyar.R
import com.metroyar.component_composable.InfoCardItem
import com.metroyar.model.InfoItem
import com.metroyar.screen.destinations.AboutUsScreenDestination
import com.metroyar.screen.destinations.FavoriteStationsScreenDestination
import com.metroyar.screen.destinations.SettingScreenDestination
import com.metroyar.ui.theme.line
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun InfoScreen(navigator: DestinationsNavigator) {
    val context = LocalContext.current
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape
    ) {
        Column {
            InfoCardItem(
                infoItem = InfoItem(
                    title = "ایستگاه های نشان شده من",
                    icon = ImageVector.vectorResource(id = R.drawable.baseline_bookmark_24_filled),
                    onClickedItem = { navigator.navigate(FavoriteStationsScreenDestination()) })
            )
            Divider(
                color = line,
                modifier = Modifier.padding(horizontal = 26.dp),
                thickness = 0.18.dp
            )
            InfoCardItem(
                infoItem = InfoItem(
                    title = "قابلیت های مترو یار",
                    icon = ImageVector.vectorResource(id = R.drawable.baseline_dashboard_24),
                    onClickedItem = {})
            )
            Divider(
                color = line,
                modifier = Modifier.padding(horizontal = 26.dp),
                thickness = 0.18.dp
            )
            InfoCardItem(
                infoItem = InfoItem(
                    title = "تنظیمات",
                    icon = Icons.Filled.Settings,
                    onClickedItem = { navigator.navigate(SettingScreenDestination()) })
            )
            Divider(
                color = line,
                modifier = Modifier.padding(horizontal = 26.dp),
                thickness = 0.18.dp
            )
            InfoCardItem(
                infoItem = InfoItem(
                    title = "درباره ما",
                    icon = Icons.Filled.Info,
                    onClickedItem = {
                        navigator.navigate(AboutUsScreenDestination())
                    })
            )
            Divider(
                color = line,
                modifier = Modifier.padding(horizontal = 26.dp),
                thickness = 0.18.dp
            )
            InfoCardItem(
                infoItem = InfoItem(
                    title = "گزارش خطا",
                    icon = Icons.Filled.Warning,
                    onClickedItem = {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:metroyarsupprt@gmail.com")
                        }
                        context.startActivity(intent)
                    })
            )
            Divider(
                color = line,
                modifier = Modifier.padding(horizontal = 26.dp),
                thickness = 0.18.dp
            )
            InfoCardItem(
                infoItem = InfoItem(
                    title = "نسخه ",
                    endText = "1.0.0",
                    icon = ImageVector.vectorResource(id = R.drawable.baseline_update_24),
                    onClickedItem = {})
            )
        }
    }
}