package com.metroyar.screen

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.metroyar.ui.theme.line
import com.metroyar.utils.GlobalObjects
import com.metroyar.utils.GlobalObjects.currentLineOfStartStation
import com.metroyar.utils.getNextTrain
import com.metroyar.utils.log
import com.metroyar.utils.minuteToLocalTime
import com.metroyar.utils.saveBitmapAndGetUri
import com.metroyar.utils.shareBitmap
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
                ExtendedFloatingActionButton(
                    onClick = {
                        showPermissionLayout = true
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P)
                            if (!shouldCapture) {
                                shouldCapture = true
                                visible = false
                            }
                    },
                    icon = { Icon(Icons.Filled.Share, "") },
                    text = { Text(text = "ارسال مسیر") },
                )
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .background(MaterialTheme.colorScheme.background)
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
    ScreenshotBox(
        modifier = Modifier.fillMaxSize(),
        screenshotState = screenshotState
    ) {
        Column {
            Spacer(Modifier.height(12.dp))
            Card(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(start = 64.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = startStation.plus(
                        " حرکت از ساعت ${
                            getNextTrain(
                                currentTime = LocalTime.now(),
                                lineNumber = currentLineOfStartStation
                            )
                        } \n" +
                                "زمان رسیدن : ${minuteToLocalTime().toStringWithCustomFormat()} "
                    ),
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )
                Spacer(modifier = Modifier.heightIn(4.dp))
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = "",
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 24.dp)
                )
                Spacer(modifier = Modifier.heightIn(4.dp))
                Text(
                    text = destinationStation,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )
            }
            Spacer(modifier = Modifier.heightIn(8.dp))
            LazyColumn {
                itemsIndexed(GlobalObjects.resultList.value) { index, item ->
                    Text(
                        item,
                        Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                    if (index < GlobalObjects.resultList.value.lastIndex)
                        Divider(color = line, thickness = 1.dp)
                }
            }
        }
    }
}