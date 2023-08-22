package com.metroyar.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.metroyar.R

fun playSound(context: Context) {
    val player = ExoPlayer.Builder(context).build()
    val resourceId = R.raw.sound
    val uri = Uri.parse("android.resource://${context.packageName}/$resourceId")
    val mediaItem = MediaItem.fromUri(uri)
    player.apply {
        setMediaItem(mediaItem)
        prepare()
        volume = 0.10f
        play()
    }
    player.addListener(object : Player.Listener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            if (playbackState == Player.STATE_ENDED)
                player.release()
        }
    })
}