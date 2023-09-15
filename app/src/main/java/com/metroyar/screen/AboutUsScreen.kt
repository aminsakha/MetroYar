package com.metroyar.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metroyar.R
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun AboutUsScreen(navigator: DestinationsNavigator) {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            modifier = Modifier.shadow(8.dp),
            title = {
                Text(text = "درباره ما")
            },
            navigationIcon = {
                IconButton(onClick = { navigator.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Go back"
                    )
                }
            })
    },
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                Text(
                    fontSize=18.sp,
                    text = stringResource(R.string.about_us),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()), textAlign = TextAlign.End
                )
            }
        }
    )
}