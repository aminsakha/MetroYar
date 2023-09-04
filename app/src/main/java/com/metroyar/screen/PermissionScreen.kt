package com.metroyar.screen

import android.Manifest
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.metroyar.composable.OneBtnAlertDialog
import com.metroyar.utils.log
import com.ramcosta.composedestinations.annotation.Destination

@Composable
fun PermissionScreen(
    onPermissionGranted: @Composable () -> Unit = {},
    onLocationDataReceived:  () -> Unit = {}
) =
    RequestSmsPermission(onPermissionGranted, onLocationDataReceived)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun RequestSmsPermission(
    onPermissionGranted: @Composable () -> Unit = {},
    onLocationDataReceived: () -> Unit = {}
) {
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.READ_MEDIA_IMAGES
        )
    )

    if (locationPermissionsState.allPermissionsGranted) {
        log("got into persmission", true)
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