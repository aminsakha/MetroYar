package com.metroyar.utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.UUID

fun shareBitmap(context: Context, uri: Uri) {
    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        setDataAndType(uri, context.contentResolver.getType(uri))
        type = "image/jpeg"
        putExtra(Intent.EXTRA_STREAM, uri)
    }
    context.startActivity(Intent.createChooser(intent, "Share image via"))
}


suspend fun saveBitmapAndGetUri(context: Context, bitmap: Bitmap): Uri? =
    withContext(Dispatchers.IO) {
        val filename = "${UUID.randomUUID()}.jpg"
        val directory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val file = File(directory, filename)

        try {
            val stream: OutputStream = FileOutputStream(file)

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

            stream.flush()
            stream.close()
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
