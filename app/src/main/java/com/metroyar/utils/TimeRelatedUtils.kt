package com.metroyar.utils

import com.metroyar.model.line.*
import java.text.NumberFormat
import java.time.Duration
import java.time.LocalTime
import java.util.Locale
import kotlin.math.roundToLong

fun LocalTime.toStringWithCustomFormat(): String {
    val persianLocale = Locale("fa", "IR")
    val numberFormat = NumberFormat.getInstance(persianLocale)
    val persianMinute = numberFormat.format(minute)
    val persianHour = numberFormat.format(hour)
    return "$persianMinute : $persianHour"
}

fun LocalTime.toMinutes(): String {
    val persianLocale = Locale("fa", "IR")
    val numberFormat = NumberFormat.getInstance(persianLocale)
    return numberFormat.format(hour * 60 + minute)
}

fun getWholeTravelTime(): LocalTime {
    val hours = GlobalObjects.bestCurrentPath!!.wholePathTime.toInt() / 60
    val minutesLeft = GlobalObjects.bestCurrentPath!!.wholePathTime.toInt() % 60
    return LocalTime.of(hours, minutesLeft)
}


fun getNextTrainArrivalTime(lineNumber: Int, currentTime: LocalTime): String {
    val metroLines =
        listOf(LineOne(), LineTwo(), LineThree(), LineFour(), LineFIve(), LineSix(), LineSeven())
    val metroLine = metroLines.find { it.number == lineNumber }

    metroLine?.let {
        for (timeChunk in it.timeTable) {
            if (currentTime.isAfter(timeChunk.start) && currentTime.isBefore(timeChunk.end)) {
                val minutesSinceChunkStart =
                    Duration.between(timeChunk.start, currentTime).toMinutes()
                val nextTrainIn =
                    timeChunk.frequency.toDouble() - (minutesSinceChunkStart % timeChunk.frequency.toDouble())

                return currentTime.plusMinutes(nextTrainIn.roundToLong()).toStringWithCustomFormat()
            }
        }
    }
    return LocalTime.of(
        5,
        30
    ).toStringWithCustomFormat()
}