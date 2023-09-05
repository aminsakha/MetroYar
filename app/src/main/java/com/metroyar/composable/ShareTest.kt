package com.metroyar.composable

import android.content.Context
import android.content.Intent
import android.net.Uri

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