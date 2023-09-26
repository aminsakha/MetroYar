package com.metroyar.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.metroyar.composable.ShouldConfirmAlertDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionScreen(
    onPermissionGranted: @Composable () -> Unit, visible: Boolean, onDismissRequest: () -> Unit,
    permissionList: List<String>, title: String, bodyMessage: String, confirmBtnText: String, shouldShowRational:(Boolean)->Unit
) {
    val locationPermissionsState = rememberMultiplePermissionsState(permissionList)
    val coroutineScope = rememberCoroutineScope()
    if (locationPermissionsState.shouldShowRationale)
        shouldShowRational.invoke(true)
    else
        shouldShowRational.invoke(false)
    if (locationPermissionsState.allPermissionsGranted)
        onPermissionGranted()
    else {
        if (visible)
            ShouldConfirmAlertDialog(
                onDismissRequest = { onDismissRequest() },
                onConfirm = {
                    coroutineScope.launch {
                        locationPermissionsState.launchMultiplePermissionRequest()
                    }
                },
                title = title,
                message = bodyMessage,
                confirmBtnText = confirmBtnText
            )
    }
}
