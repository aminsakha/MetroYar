package com.metroyar.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionDialog(
    onPermissionGranted: @Composable () -> Unit, visible: Boolean, onDismissRequest: () -> Unit,
    permissionList: List<String>, title: String, bodyMessage: String, confirmBtnText: String, shouldShowRational:(Boolean)->Unit
) {
    val permissionsState = rememberMultiplePermissionsState(permissionList)
    val coroutineScope = rememberCoroutineScope()
    if (permissionsState.shouldShowRationale)
        shouldShowRational.invoke(true)
    else
        shouldShowRational.invoke(false)
    if (permissionsState.allPermissionsGranted)
        onPermissionGranted()
    else {
        if (visible)
            ShouldConfirmAlertDialog(
                onDismissRequest = { onDismissRequest() },
                onConfirm = {
                    coroutineScope.launch {
                        permissionsState.launchMultiplePermissionRequest()
                    }
                },
                title = title,
                message = bodyMessage,
                confirmBtnText = confirmBtnText
            )
    }
}
