package com.metroyar.screen

import android.Manifest
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.metroyar.composable.OneBtnAlertDialog
import com.metroyar.utils.log
import com.ramcosta.composedestinations.annotation.Destination


@Destination
@Composable
fun PermissionScreen(
    onPermissionGranted: () -> Unit = {},
    onPermissionGrantedNextScreen: @Composable () -> Unit = {}
) =
    RequestSmsPermission(onPermissionGranted, onPermissionGrantedNextScreen)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun RequestSmsPermission(
    onPermissionGranted: () -> Unit = {},
    onPermissionGrantedNextScreen: @Composable () -> Unit = {}
) {
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    if (locationPermissionsState.allPermissionsGranted) {
        log("got into persmission",true)
        onPermissionGranted()
        onPermissionGrantedNextScreen()
    } else {
        OneBtnAlertDialog(
            onConfirm = { locationPermissionsState.launchMultiplePermissionRequest() },
            title = "مکان",
            message = "مکانتو بده دیگه",
            okMessage = "اوکیه"
        )
    }
}