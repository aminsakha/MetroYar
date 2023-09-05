package com.metroyar.screen

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.metroyar.composable.OneBtnAlertDialog
import com.metroyar.utils.log
import com.ramcosta.composedestinations.annotation.Destination

@Composable
fun PermissionScreen(
    onPermissionGranted: @Composable () -> Unit = {},
    onLocationDataReceived: () -> Unit = {}
) =
    RequestSmsPermission(onPermissionGranted, onLocationDataReceived)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun RequestSmsPermission(
    onPermissionGranted: @Composable () -> Unit = {},
    onLocationDataReceived: () -> Unit = {}
) {
    val locationPermissionsState = rememberMultiplePermissionsState(
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        else
            listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
    )

    if (locationPermissionsState.allPermissionsGranted) {
        onPermissionGranted()
        onLocationDataReceived()
    } else {
        OneBtnAlertDialog(
            onConfirm = { locationPermissionsState.launchMultiplePermissionRequest() },
            title = "اجازه مکان",
            message = "مکانتو بده دیگه",
            okMessage = "اوکیه"
        )
    }
}