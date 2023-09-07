package com.metroyar.screen

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.metroyar.composable.shareBitmap
import com.metroyar.ui.theme.line
import com.metroyar.utils.GlobalObjects
import com.metroyar.utils.log
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.smarttoolfactory.screenshot.ImageResult
import com.smarttoolfactory.screenshot.ScreenshotBox
import com.smarttoolfactory.screenshot.ScreenshotState
import com.smarttoolfactory.screenshot.rememberScreenshotState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Destination
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PathResultScreen(navigator: DestinationsNavigator) {
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

            is ImageResult.Error -> {

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
                //shouldCapture = false
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

                bodyMessage = "برای ارسال عکس نیاز به دسترسی فضای ذخیره سازی داریم",
                confirmBtnText = "اوکیه"
            )
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
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
                extracted(screenshotState)
                uri?.let { shareBitmap(context, it) }
            }
        }
    )
}

@Composable
fun extracted(screenshotState: ScreenshotState) {
    ScreenshotBox(
        modifier = Modifier.fillMaxSize(),
        screenshotState = screenshotState
    ) {
        Column {
            Spacer(Modifier.height(12.dp))

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