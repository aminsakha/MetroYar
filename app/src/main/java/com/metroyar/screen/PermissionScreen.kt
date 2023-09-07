package com.metroyar.screen

import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.metroyar.composable.OneBtnAlertDialog

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionScreen(
    onPermissionGranted: @Composable () -> Unit, visible: Boolean, onDismissRequest: () -> Unit,
    permissionList: List<String>, title: String, bodyMessage: String, confirmBtnText: String
) {
    val locationPermissionsState = rememberMultiplePermissionsState(permissionList)

    if (locationPermissionsState.allPermissionsGranted)
        onPermissionGranted()
    else {
        if (visible)
            OneBtnAlertDialog(
                onDismissRequest = { onDismissRequest() },
                onConfirm = { locationPermissionsState.launchMultiplePermissionRequest() },
                title = title,
                message = bodyMessage,
                confirmBtnText = confirmBtnText
            )
    }
}
