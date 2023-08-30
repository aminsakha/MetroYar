package com.metroyar.screen

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.IntentSender
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import com.metroyar.utils.GlobalObjects
import com.metroyar.utils.GlobalObjects.locationFlow
import com.metroyar.utils.getCurrentLocation
import com.metroyar.utils.log
import com.metroyar.utils.setTextFieldsWithApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun Layout() {
    val context: Context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val settingResultRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            log("res 2 ", GlobalObjects.UserLatitude.value)
            coroutineScope.launch {
                withContext(Dispatchers.Main) {
                    getCurrentLocation(context)
                }
                withContext(Dispatchers.IO) {
                    locationFlow.collect { location ->
                        if (location != null) {
                            log("res 22 ", location.x)
                            setTextFieldsWithApiResponse()
                        }
                    }
                }
            }
        } else
            log("appDebug", "Denied")
    }
    checkLocationSetting(
        context = context,
        onDisabled = { intentSenderRequest ->
            settingResultRequest.launch(intentSenderRequest)
        },
        onEnabled = { /* This will call when setting is already enabled */ }
    )
}

fun checkLocationSetting(
    context: Context,
    onDisabled: (IntentSenderRequest) -> Unit,
    onEnabled: () -> Unit
) {
    val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        Integer.MAX_VALUE.toLong()
    )
        .setWaitForAccurateLocation(false)
        .setMinUpdateIntervalMillis(1000)
        .setMaxUpdateDelayMillis(100)
        .build()
    val client: SettingsClient = LocationServices.getSettingsClient(context)
    val builder: LocationSettingsRequest.Builder = LocationSettingsRequest
        .Builder()
        .addLocationRequest(locationRequest)

    val gpsSettingTask: Task<LocationSettingsResponse> =
        client.checkLocationSettings(builder.build())

    gpsSettingTask.addOnSuccessListener { onEnabled() }
    gpsSettingTask.addOnFailureListener { exception ->
        if (exception is ResolvableApiException) {
            try {
                val intentSenderRequest = IntentSenderRequest
                    .Builder(exception.resolution)
                    .build()
                onDisabled(intentSenderRequest)
            } catch (sendEx: IntentSender.SendIntentException) {
                // ignore here
            }
        }
    }
}