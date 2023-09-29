package com.metroyar.utils

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun playSound(context: Context, soundResourceId: Int,volumeRange:Float=1f) {
    val player = ExoPlayer.Builder(context).build()
    val uri = Uri.parse("android.resource://${context.packageName}/$soundResourceId")
    val mediaItem = MediaItem.fromUri(uri)
    player.apply {
        setMediaItem(mediaItem)
        prepare()
        volume = volumeRange
        play()
    }
    player.addListener(object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            if (playbackState == Player.STATE_ENDED)
                player.release()
        }
    })
}