package com.metroyar.screen

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.metroyar.composable.ShareableImage
import com.metroyar.utils.log
import com.smarttoolfactory.screenshot.ImageResult
import com.smarttoolfactory.screenshot.ScreenshotBox
import com.smarttoolfactory.screenshot.ScreenshotState
import com.smarttoolfactory.screenshot.rememberScreenshotState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.UUID


@Composable
fun AccountScreen() {
    ScreenshotDemo()
}

@Composable
fun ScreenshotDemo() {

    val screenshotState = rememberScreenshotState()
    var showDialog by remember { mutableStateOf(false) }
    var uri by remember { mutableStateOf<Uri?>(null) }
    val imageResult: ImageResult = screenshotState.imageState.value
    val context = LocalContext.current
    // Show dialog only when ImageResult is success or error
    LaunchedEffect(key1 = imageResult) {
        if (imageResult is ImageResult.Success || imageResult is ImageResult.Error) {
            showDialog = true
        }
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = {
                screenshotState.capture()
            }) {
                Text(text = "Capture")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        content = { paddingValues: PaddingValues ->
            ScreenshotSample(screenshotState, paddingValues)

            if (showDialog) {
                ImageAlertDialog(
                    imageResult = imageResult,
                    context = context,
                    onUri = { uri = it }) {
                    showDialog = false
                }
            }
            uri?.let { ShareableImage(it) }
        }
    )
}

@Composable
private fun ScreenshotSample(screenshotState: ScreenshotState, paddingValues: PaddingValues) {
    Column(
        modifier = Modifier.background(Color(0xffECEFF1))
    ) {

        Spacer(modifier = Modifier.height(30.dp))

        ScreenshotBox(
            modifier = Modifier.fillMaxSize(),
            screenshotState = screenshotState
        ) {
            Text(text = "hellow")
        }
    }
}

@Composable
private fun ImageAlertDialog(
    onUri: (Uri) -> Unit,
    context: Context,
    imageResult: ImageResult,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            FilledTonalButton(onClick = { onDismiss() }) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            FilledTonalButton(onClick = { onDismiss() }) {
                Text(text = "Dismiss")
            }
        },
        text = {
            when (imageResult) {
                is ImageResult.Success -> {
                    Image(bitmap = imageResult.data.asImageBitmap(), contentDescription = null)
                        LaunchedEffect(key1 = true) {
                            log("into laun", true)
                            val res = saveBitmapAndGetUri(
                                bitmap = imageResult.data.asImageBitmap().asAndroidBitmap(),
                                context = context
                            )
                            if (res != null) {
                                onUri.invoke(res)
                            }
                            log("res", res)
                        }
                }

                is ImageResult.Error -> {
                    Text(text = "Error: ${imageResult.exception.message}")
                }

                else -> {}
            }
        })
}

suspend fun saveBitmapAndGetUri(context: Context, bitmap: Bitmap): Uri? =
    withContext(Dispatchers.IO) {
        // Generate a random filename
        val filename = "${UUID.randomUUID()}.jpg"

        // Get the external storage Downloads directory
        val directory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        // Create a new file in the downloads directory
        val file = File(directory, filename)

        try {
            // Open the file output stream
            val stream: OutputStream = FileOutputStream(file)

            // Compress the bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

            // Flush and close the stream
            stream.flush()
            stream.close()
            log("flsh", true)
            // Get the content URI for the file
            val contentUri =
                getImageContentUri(context, file.absolutePath)

            contentUri
        } catch (e: Exception) {
            log("error", e.message)
            null
        }
    }

@SuppressLint("Range")
fun getImageContentUri(context: Context, imagePath: String): Uri? {
    val cursor = context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        arrayOf(MediaStore.Images.Media._ID),
        MediaStore.Images.Media.DATA + "=? ",
        arrayOf(imagePath),
        null
    )

    return if (cursor != null && cursor.moveToFirst()) {
        val id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID))
        val baseUri = Uri.parse("content://media/external/images/media")
        Uri.withAppendedPath(baseUri, "" + id)
    } else {
        if (File(imagePath).exists()) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.DATA, imagePath)
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        } else {
            null
        }
    }
}
