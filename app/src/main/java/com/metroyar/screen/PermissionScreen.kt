package com.metroyar.screen

import android.Manifest
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.metroyar.R
import com.metroyar.composable.OneBtnAlertDialog
import com.metroyar.utils.log
import com.ramcosta.composedestinations.annotation.Destination


@Destination
@Composable
fun PermissionScreen(onPermissionGranted: () -> Unit) =
    RequestSmsPermission(onPermissionGranted)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun RequestSmsPermission(onPermissionGranted: () -> Unit) {
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    if (locationPermissionsState.allPermissionsGranted) {
        onPermissionGranted()
    } else {
        OneBtnAlertDialog(
            onConfirm = { locationPermissionsState.launchMultiplePermissionRequest() },
            title = "مکان",
            message = "مکانتو بده دیگه",
            okMessage = "اوکیه"
        )
    }
}