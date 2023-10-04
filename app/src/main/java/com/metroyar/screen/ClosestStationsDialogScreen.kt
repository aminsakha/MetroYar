package com.metroyar.screen

import android.Manifest
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.airbnb.lottie.compose.LottieClipSpec
import com.metroyar.R
import com.metroyar.composable.EnableLocationDialog
import com.metroyar.composable.PermissionDialog
import com.metroyar.composable.ShouldConfirmAlertDialog
import com.metroyar.composable.UserClosestStationsDialog
import com.metroyar.model.GPSCoordinate
import com.metroyar.utils.*


@Composable
fun ClosestStationsDialogScreen(
    context: Context,
    onDstClicked: (String) -> Unit,
    onSrcClicked: (String) -> Unit,
    onDisMiss: (Boolean) -> Unit
) {
    var showSuggestionDialog by remember { mutableStateOf(true) }
    var shouldShowRash by remember { mutableStateOf(false) }
    var showInternetDialog by remember { mutableStateOf(true) }
    var clipSpec by remember { mutableStateOf(LottieClipSpec.Progress(0.0f, 0.31f)) }
    var isLocEnabled by remember { mutableStateOf(false) }
    var shouldShowPermission by remember { mutableStateOf(true) }
    var isWiFiEnabled by remember { mutableStateOf(false) }
    var location by remember { mutableStateOf(GPSCoordinate(0.0, 0.0)) }
    var closestStationsPair by remember { mutableStateOf(Pair("empty", "empty")) }

    UserClosestStationsDialog(
        onDismissRequest = {
            showSuggestionDialog = false
            onDisMiss.invoke(false)
        },
        pairOfClosestStations = convertNeshanStationNameToMyFormat(closestStationsPair),
        visible = showSuggestionDialog,
        srcOnclick = {
            showSuggestionDialog = false
            onSrcClicked.invoke(it)
        },
        dstOnClicked = {
            showSuggestionDialog = false
            onDstClicked.invoke(it)
        },
        clipSpec = clipSpec
    )

    PermissionDialog(
        visible = shouldShowPermission,
        permissionList = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ),
        onDismissRequest = {
            shouldShowPermission = false
            onDisMiss.invoke(false)
        },
        title = stringResource(R.string.enableLoc),
        bodyMessage = if (!shouldShowRash) stringResource(R.string.enableLocMessage) else stringResource(
            R.string.enableLocMessageRash
        ),
        confirmBtnText = stringResource(R.string.confirmBtn),
        shouldShowRational = { shouldShowRash = it },
        onPermissionGranted = {
            shouldShowPermission = false
            if (!checkGpsStatus(context))
                EnableLocationDialog {
                    isLocEnabled = it
                    if (!isLocEnabled)
                        onDisMiss.invoke(false)
                }
            else
                isLocEnabled = true
        }
    )
    LaunchedEffect(key1 = isWiFiEnabled) {
        checkInternetConnection(context, onStatChange = { isWiFiEnabled = it })
    }

    if (!isWiFiEnabled && location.x == 0.0) {
        ShouldConfirmAlertDialog(
            visible = showInternetDialog,
            onConfirm = { showInternetDialog = false },
            onDismissRequest = {
                showInternetDialog = false
            },
            title = stringResource(R.string.netLostTitle),
            message = stringResource(R.string.internetLostMessage),
            confirmBtnText = stringResource(R.string.confirmBtn)
        )
    }
    if (isWiFiEnabled && isLocEnabled && location.x == 0.0) {
        GetCurrentLocation(context, onLocationChange = { location = it })
    }

    LaunchedEffect(key1 = location) {
        if (location.x != 0.0)
            getClosestStationsFromApi(
                location = location,
                onPairChange = { closestStationsPair = it })
    }

    if (closestStationsPair.first != "empty") {
        log("res from Neshan", closestStationsPair)
        clipSpec = LottieClipSpec.Progress(0.0f, 1f)
    }
}