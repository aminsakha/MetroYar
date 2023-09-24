package com.metroyar.screen

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.metroyar.R
import com.metroyar.classes.UserFriendlyPathStyle
import com.metroyar.component_composable.ArrivalsTime
import com.metroyar.component_composable.ExpandableCard
import com.metroyar.component_composable.SrcAndDstCard
import com.metroyar.ui.theme.lineFive
import com.metroyar.ui.theme.lineFour
import com.metroyar.ui.theme.lineOne
import com.metroyar.ui.theme.lineSeven
import com.metroyar.ui.theme.lineSix
import com.metroyar.ui.theme.lineThree
import com.metroyar.ui.theme.lineTwo
import com.metroyar.ui.theme.redd
import com.metroyar.ui.theme.turnedOff2
import com.metroyar.utils.GlobalObjects.bestCurrentPath
import com.metroyar.utils.GlobalObjects.resultList
import com.metroyar.utils.getNextTrain
import com.metroyar.utils.log
import com.metroyar.utils.minuteToLocalTime
import com.metroyar.utils.saveBitmapAndGetUri
import com.metroyar.utils.shareBitmap
import com.metroyar.utils.toMinutes
import com.metroyar.utils.toStringWithCustomFormat
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.smarttoolfactory.screenshot.*
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
            PermissionScreen(
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
                        modifier = Modifier.padding(2.dp),
                        horizontalArrangement = Arrangement.Center
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
                BestPathLayout(screenshotState, startStation, destinationStation)
                uri?.let { shareBitmap(context, it) }
            }
        }
    )
}

@Composable
fun BestPathLayout(
    screenshotState: ScreenshotState,
    startStation: String,
    destinationStation: String
) {
    ScreenshotBox(screenshotState = screenshotState) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(end = 16.dp, top = 8.dp, start = 16.dp)
        ) {
            Spacer(Modifier.height(8.dp))
            Box(modifier = Modifier.size(200.dp, 100.dp), contentAlignment = Alignment.CenterEnd) {
                SrcAndDstCard(src = startStation, dst = destinationStation)
            }

            Spacer(Modifier.height(12.dp))
            ArrivalsTime(
                trainArrivalTime = " ساعت رسیدن مترو به مبدا : ${
                    getNextTrain(
                        currentTime = LocalTime.now(),
                        lineNumber = bestCurrentPath!!.stationsOnPath[0].lineNumber
                    )
                } ", pathTime = "زمان سفر : ${minuteToLocalTime().toMinutes()} دقیقه"
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(horizontalAlignment = Alignment.End) {
                itemsIndexed(UserFriendlyPathStyle(resultList.value).result) { index, item ->
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_circle_24),
                            contentDescription = "",
                            tint = if (index != UserFriendlyPathStyle(resultList.value).result.lastIndex) getLineColor(
                                UserFriendlyPathStyle(resultList.value).expandableItems[item]!![0]
                            ) else getLineColor(resultList.value.last())
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        ExpandableCard(
                            title = item,
                            userFriendlyPathStyle = UserFriendlyPathStyle(resultList.value)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
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
        else -> turnedOff2
    }
}