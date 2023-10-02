package com.metroyar.screen

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.metroyar.R
import com.metroyar.classes.GuidPathStyle
import com.metroyar.composable.ArrivalsTime
import com.metroyar.composable.ExpandableCardForGuidPathStyle
import com.metroyar.composable.PermissionDialog
import com.metroyar.composable.SrcAndDstCard
import com.metroyar.ui.theme.lineFive
import com.metroyar.ui.theme.lineFour
import com.metroyar.ui.theme.lineOne
import com.metroyar.ui.theme.lineSeven
import com.metroyar.ui.theme.lineSix
import com.metroyar.ui.theme.lineThree
import com.metroyar.ui.theme.lineTwo
import com.metroyar.ui.theme.zahrasBlack
import com.metroyar.utils.GlobalObjects.bestCurrentPath
import com.metroyar.utils.GlobalObjects.readableFormResultList
import com.metroyar.utils.getNextTrainArrivalTime
import com.metroyar.utils.log
import com.metroyar.utils.getWholeTravelTime
import com.metroyar.utils.saveBitmapAndGetUri
import com.metroyar.utils.shareBitmap
import com.metroyar.utils.toMinutes
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.smarttoolfactory.screenshot.ImageResult
import com.smarttoolfactory.screenshot.ScreenshotBox
import com.smarttoolfactory.screenshot.ScreenshotState
import com.smarttoolfactory.screenshot.rememberScreenshotState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalTime

@Destination
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PathResultScreen(
    navigator: DestinationsNavigator,
    startStation: String,
    destinationStation: String
) {
    var showPermissionLayout by remember { mutableStateOf(false) }
    var shouldShowPermission by remember { mutableStateOf(true) }
    var visible by remember { mutableStateOf(true) }
    var shouldCapture by remember { mutableStateOf(false) }
    val screenshotState = rememberScreenshotState()
    var uri by remember { mutableStateOf<Uri?>(null) }
    val imageResult: ImageResult = screenshotState.imageState.value
    val context = LocalContext.current

    LaunchedEffect(key1 = imageResult) {
        when (imageResult) {
            is ImageResult.Success -> {
                val res = saveBitmapAndGetUri(
                    bitmap = imageResult.data.asImageBitmap().asAndroidBitmap(),
                    context = context
                )
                if (res != null) {
                    uri = res
                    log("uri", uri)
                }
            }

            else -> {}
        }
    }

    DisposableEffect(shouldCapture) {
        if (shouldCapture) {
            CoroutineScope(Dispatchers.Main).launch {
                delay(10)
                screenshotState.capture()
                visible = true
            }
        }
        onDispose { }
    }

    if (showPermissionLayout) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P)
            PermissionDialog(
                visible = shouldShowPermission,
                onDismissRequest = { shouldShowPermission = false },
                onPermissionGranted = {
                    shouldShowPermission = false
                    if (!shouldCapture) {
                        shouldCapture = true
                        visible = false
                    }
                },
                permissionList = listOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                title = "دسترسی به فضای دستگاه",
                bodyMessage = "برای ارسال عکس نیاز به دسترسی فضای ذخیره سازی داریم اگر دسترسی را قطع کنید دفعات بعدی باید به صورت دستی این دسترسی را بدهید وگرنه این قابلیت کار نخواهد کرد",
                confirmBtnText = "اوکیه", shouldShowRational = {}
            )
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            modifier = Modifier.shadow(8.dp),
            title = {
                Text(text = "بهترین مسیر")
            },
            navigationIcon = {
                IconButton(onClick = { navigator.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Go back"
                    )
                }
            })
    },
        floatingActionButton = {
            if (visible)
                ElevatedButton(
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    shape = FloatingActionButtonDefaults.largeShape,
                    onClick = {
                        showPermissionLayout = true
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P)
                            if (!shouldCapture) {
                                shouldCapture = true
                                visible = false
                            }
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(1.1.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "ارسال مسیر")
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Filled.Share, "")
                    }
                }
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(MaterialTheme.colorScheme.onPrimary),
                horizontalAlignment = Alignment.End
            ) {
                BestPathLayout(
                    screenshotState,
                    startStation,
                    destinationStation,
                    navigator = navigator
                )
                uri?.let { shareBitmap(context, it) }
            }
        }
    )
}

@Composable
fun BestPathLayout(
    screenshotState: ScreenshotState,
    startStation: String,
    destinationStation: String, navigator: DestinationsNavigator
) {
    ScreenshotBox(screenshotState = screenshotState) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(end = 16.dp, top = 8.dp, start = 16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            SrcAndDstCard(
                context = LocalContext.current,
                navigator = navigator,
                src = startStation,
                dst = destinationStation
            )

            Spacer(Modifier.height(8.dp))
            ArrivalsTime(
                trainArrivalTime = " ساعت رسیدن مترو به مبدا : ${
                    getNextTrainArrivalTime(
                        currentTime = LocalTime.now(),
                        lineNumber = bestCurrentPath!!.stationsOnPath[0].lineNumber
                    )
                } ", pathTime = "زمان سفر : ${getWholeTravelTime().toMinutes()} دقیقه"
            )

            Spacer(modifier = Modifier.height(24.dp))

            val guidPathStyleInstance = GuidPathStyle(readableFormResultList)

            LazyColumn(horizontalAlignment = Alignment.End) {
                itemsIndexed(guidPathStyleInstance.guidPathStyleStringList) { index, item ->
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (item.contains("خط"))
                            Icon(
                                painter = painterResource(id = R.drawable.swap),
                                modifier = Modifier.size(24.dp),
                                contentDescription = "",
                            )
                        else
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_circle_24),
                                contentDescription = "",
                                tint = if (index != guidPathStyleInstance.guidPathStyleStringList.lastIndex) getLineColor(
                                    guidPathStyleInstance.mapOfGuidPathToItsChildren[item]!![0]
                                ) else getLineColor(readableFormResultList.last())
                            )
                        Spacer(modifier = Modifier.width(12.dp))
                        ExpandableCardForGuidPathStyle(
                            title = item,
                            guidPathStyle = GuidPathStyle(readableFormResultList)
                        )
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                }
            }
        }
    }
}

fun getLineColor(currStation: String): Color {
    return when (bestCurrentPath!!.stationsOnPath.distinctBy { it.stationName }
        .find { currStation.contains(it.stationName) }?.lineNumber) {
        1 -> lineOne
        2 -> lineTwo
        3 -> lineThree
        4 -> lineFour
        5 -> lineFive
        6 -> lineSix
        7 -> lineSeven
        else -> zahrasBlack
    }
}