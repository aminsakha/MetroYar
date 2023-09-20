package com.metroyar.component_composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCancellationBehavior
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.metroyar.R
import com.metroyar.utils.GlobalObjects.deviceHeightInDp
import com.metroyar.utils.GlobalObjects.deviceWidthInDp
import com.metroyar.utils.log


@Composable
fun ShowLottieAnimation(animationRawId: Int, speed: Float = 1f,clipSpec: LottieClipSpec) {
    var isPlaying by remember {
        mutableStateOf(true)
    }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animationRawId))
    val progressShit by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        speed = speed,
        cancellationBehavior = LottieCancellationBehavior.OnIterationFinish,
        clipSpec = clipSpec,
        isPlaying = isPlaying,
        restartOnPlay = false
    )

    LaunchedEffect(key1 = progressShit) {
        if (progressShit == 0f)
            log("zero", true)
        if (progressShit > 0.99f)
            isPlaying = false
    }
    LottieAnimation(
        composition,
        progressShit,
        modifier = Modifier.size((deviceHeightInDp + deviceWidthInDp) / 8)
    )
    Column(
        Modifier
            .background(Color.White)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                //clipSpec = LottieClipSpec.Progress(0f, 1f)
            },
        ) {
            Text(
                text = if (isPlaying) "Pause" else "Play",
                color = Color.White
            )
        }
    }
}

@Composable
fun StationsDialog(
    onDismissRequest: () -> Unit = {},
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(deviceHeightInDp.times(0.4f)),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
        ) {
            ShowLottieAnimation(R.raw.station_loading_animation, isPlaying = true)
        }
    }
}