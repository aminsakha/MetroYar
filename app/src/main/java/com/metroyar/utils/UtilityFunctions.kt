package com.metroyar.utils

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.Rect
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metroyar.R
import com.metroyar.ui.theme.textColor
import com.metroyar.utils.GlobalObjects.TAG
import com.metroyar.utils.GlobalObjects.stationList
import kotlinx.coroutines.delay

fun findStationObjectFromItsName(stationName: String) =
    stationList.filter { it.stationName == stationName }

fun findStationObjectFromItsId(stationId: Int) = stationList.find { it.id == stationId }!!

fun toastMaker(
    context: Context,
    text: String
) {
    Toast.makeText(
        context,
        text,
        Toast.LENGTH_SHORT
    ).show()
}

fun log(stringMessage: String = "", wantToLogThis: Any?) =
    Log.d(TAG, "$stringMessage : $wantToLogThis")

fun checkInternetConnection(context: Context, onStatChange: (Boolean) -> Unit) {
    val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()
    val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            log("network stat", "okeye")
            onStatChange.invoke(true)
            super.onAvailable(network)
        }

        override fun onLost(network: Network) {
            log("network stat", "ok nist")
            onStatChange.invoke(false)
            super.onLost(network)
        }
    }
    val connectivityManager =
        context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
    if (connectivityManager.activeNetwork == null)
        onStatChange.invoke(false)
    else
        onStatChange.invoke(true)
    connectivityManager.requestNetwork(networkRequest, networkCallback)
}

fun vibratePhone(context: Context) {
    val vibrator = context.getSystemService(Vibrator::class.java)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    20,
                    VibrationEffect.EFFECT_TICK
                )
            )
        }
    }
}

@Composable
fun BackPressAction() {
    var exit by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(key1 = exit) {
        if (exit) {
            delay(1000)
            exit = false
        }
    }

    BackHandler(enabled = true) {
        if (exit) {
            context.startActivity(Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_HOME)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
        } else {
            exit = true
            Toast.makeText(context, "برای خروج دوباره کلیک کنید", Toast.LENGTH_SHORT).show()
        }
    }
}

fun Modifier.nonRipple(onclick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember {
            MutableInteractionSource()
        }) {
        onclick()
    }
}

@Composable
fun CircleWithText(text: String, color: Color) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .background(Color.White, RoundedCornerShape(10.dp))
            .border(2.5.dp, color = color, RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = " خط $text",
            fontSize = 9.sp,
            fontFamily = FontFamily(Font(R.font.iran_medium)),
            color = textColor,
            maxLines = 1,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(1.dp)
        )
    }
}
