package com.metroyar.screen

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.metroyar.R
import com.metroyar.classes.BestPathResult
import com.metroyar.component_composable.ShouldConfirmAlertDialog
import com.metroyar.component_composable.AutoCompleteOutLinedTextField
import com.metroyar.screen.destinations.PathResultScreenDestination
import com.metroyar.utils.GlobalObjects.destStation
import com.metroyar.utils.GlobalObjects.resultList
import com.metroyar.utils.GlobalObjects.startStation
import com.metroyar.utils.GlobalObjects.stationList
import com.metroyar.utils.SuggestionStationsLayout
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun NavigationScreen(context: Context, navigator: DestinationsNavigator) {
    var srcInputText by remember { mutableStateOf(startStation) }
    var dstInputText by remember { mutableStateOf(destStation) }
    var alertMessageText by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var isFindNearestButtonClicked by remember { mutableStateOf(false) }
    val focusRequesterDst = remember { FocusRequester() }
    val focusRequesterSrc = remember { FocusRequester() }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LaunchedEffect(srcInputText) {
            if (stationList.map { it.stationName }
                    .contains(srcInputText) && dstInputText.isEmpty())
                focusRequesterDst.requestFocus()
        }

        AutoCompleteOutLinedTextField(
            label = "ایستگاه مبدا رو انتخاب کن",
            focusRequester = focusRequesterSrc,
            inputValue = srcInputText,
            onInputValueChange = {
                srcInputText = it
                startStation = srcInputText
            },
            onItemSelectedChange = {
                srcInputText = it
                startStation = srcInputText
            },
            onTrashIconClick = { srcInputText = "" }
        )

        Spacer(Modifier.height(16.dp))

        AutoCompleteOutLinedTextField(
            label = "ایستگاه مقصد رو انتخاب کن",
            focusRequester = focusRequesterDst,
            inputValue = dstInputText,
            onInputValueChange = {
                dstInputText = it
                destStation = dstInputText
            },
            onItemSelectedChange = {
                dstInputText = it
                destStation = dstInputText
            },
            onTrashIconClick = { dstInputText = "" }
        )

        Spacer(Modifier.height(16.dp))
        if (isFindNearestButtonClicked) {
            SuggestionStationsLayout(
                onDisMiss = { isFindNearestButtonClicked = it },
                onSuggestionStationsDialogDisMiss = { isFindNearestButtonClicked = it },
                context = context,
                onSrcClicked = {
                    srcInputText = it
                    startStation = srcInputText
                    isFindNearestButtonClicked = false
                },
                onDstClicked = {
                    dstInputText = it
                    destStation = dstInputText
                    isFindNearestButtonClicked = false
                })
        }

        Button(onClick = {
            isFindNearestButtonClicked = true
        }) {
            Text(text = "نزدیکترین ایستگاه به من")
        }
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                if (srcInputText == dstInputText && (srcInputText.isNotEmpty() && dstInputText.isNotEmpty())) {
                    alertMessageText = " اشتباهی مبدا و مقصد رو یکی زدی "
                    showDialog = true
                } else {
                    if (stationList.map { it.stationName }
                            .containsAll(Pair(srcInputText, dstInputText).toList())) {
                        resultList.value = BestPathResult(
                            context,
                            srcInputText,
                            dstInputText
                        ).convertPathToUserUnderstandableForm()
                        navigator.navigate(PathResultScreenDestination)
                    } else {
                        alertMessageText = " مبدا و مقصد رو درست بزن لطفا "
                        showDialog = true
                    }
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("برام بهترین مسیرو پیدا کن", color = MaterialTheme.colorScheme.onPrimary)
        }
        ShouldConfirmAlertDialog(
            visible = showDialog,
            onConfirm = { showDialog = false },
            onDismissRequest = { showDialog = false },
            title = stringResource(R.string.notice_text),
            message = alertMessageText,
            confirmBtnText = stringResource(R.string.ok_message)
        )
    }
}