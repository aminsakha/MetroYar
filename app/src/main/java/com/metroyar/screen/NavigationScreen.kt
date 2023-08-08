package com.metroyar.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import com.metroyar.classes.Result


@Composable
fun NavigationScreen() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text =
            Result(LocalContext.current, "میدان صنعت", "قلهک").convertPathToUserUnderstandableForm()
                .toString(), textAlign = TextAlign.End
        )
    }
}