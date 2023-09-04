package com.metroyar.composable

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun ShareableImage(uri: Uri) {
    val context = LocalContext.current
    val shareIcon = Icons.Default.Share

    IconButton(onClick = { shareBitmap(context, uri) }) {
        Icon(shareIcon, contentDescription = "Share")
    }
}

fun shareBitmap(context: Context, uri:Uri) {

    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        setDataAndType(uri, context.contentResolver.getType(uri))
        type = "image/jpeg"
        putExtra(Intent.EXTRA_STREAM, uri)
    }

    context.startActivity(Intent.createChooser(intent, "Share image via"))
}