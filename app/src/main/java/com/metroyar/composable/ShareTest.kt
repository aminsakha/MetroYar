package com.metroyar.composable

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

@Composable
fun ShareableImage(bitmap: Bitmap) {
    val context = LocalContext.current
    val shareIcon = Icons.Default.Share

    IconButton(onClick = { shareBitmap(context, bitmap) }) {
        Icon(shareIcon, contentDescription = "Share")
    }
}

fun shareBitmap(context: Context, bitmap: Bitmap) {
    val file = File(context.cacheDir, "${System.currentTimeMillis()}.png")
    val fos = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
    fos.flush()
    fos.close()
    file.setReadable(true, false)


    val contentUri = FileProvider.getUriForFile(context, "com.example.yourapp.fileprovider", file)

    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        setDataAndType(contentUri, context.contentResolver.getType(contentUri))
        type = "image/jpeg"
        putExtra(Intent.EXTRA_STREAM, contentUri)
    }

    context.startActivity(Intent.createChooser(intent, "Share image via"))
}