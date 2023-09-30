package com.metroyar.composable

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCancellationBehavior
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.metroyar.utils.GlobalObjects.deviceHeightInDp
import com.metroyar.utils.GlobalObjects.deviceWidthInDp


@Composable
fun ShowLottieAnimation(
    animationRawId: Int,
    speed: Float = 1f,
    clipSpec: LottieClipSpec,
    iterations:Int=LottieConstants.IterateForever,
    onAnimationFinished: (Boolean) -> Unit,
    animationSize: Dp=(deviceHeightInDp + deviceWidthInDp) / 4,
    shouldStopAnimation: Boolean = true
) {
    var isPlaying by remember {
        mutableStateOf(true)
    }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animationRawId))
    val progressShit by animateLottieCompositionAsState(
        composition,
        iterations = iterations,
        speed = speed,
        cancellationBehavior = LottieCancellationBehavior.OnIterationFinish,
        clipSpec = clipSpec,
        isPlaying = isPlaying,
        restartOnPlay = true
    )

    LaunchedEffect(key1 = progressShit) {
        if (shouldStopAnimation) {
            if (progressShit > 0.5)
                onAnimationFinished.invoke(true)
            if (progressShit > 0.99f)
                isPlaying = false
        }
    }
    LottieAnimation(
        composition,
        progressShit,
        modifier = Modifier.size(animationSize)
    )
}