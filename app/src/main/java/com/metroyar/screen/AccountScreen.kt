package com.metroyar.screen

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.google.accompanist.permissions.ExperimentalPermissionsApi


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AccountScreen() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Account")
    }
}