package com.metroyar.screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.metroyar.R
import com.metroyar.classes.BestPathResult
import com.metroyar.composable.AutoCompleteOutLinedTextField
import com.metroyar.composable.ShouldConfirmAlertDialog
import com.metroyar.screen.destinations.PathResultScreenDestination
import com.metroyar.ui.theme.textColor
import com.metroyar.utils.BackPressAction
import com.metroyar.utils.GlobalObjects.destStation
import com.metroyar.utils.GlobalObjects.deviceHeightInDp
import com.metroyar.utils.GlobalObjects.readableFormResultList
import com.metroyar.utils.GlobalObjects.startStation
import com.metroyar.utils.GlobalObjects.stationList
import com.metroyar.utils.playSound
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
    val state = rememberScrollState()
    Scaffold(
        floatingActionButton = {
            ElevatedButton(
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                shape = FloatingActionButtonDefaults.largeShape,
                modifier = Modifier.padding(bottom = 20.dp, end = 8.dp),
                onClick = { isFindNearestButtonClicked = true },
            ) {
                Row(
                    verticalAlignment=Alignment.CenterVertically,
                    //modifier = Modifier.padding(1.2.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "ایستگاه های نزدیک", color = textColor,
                        style = MaterialTheme.typography.displayMedium
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.train_landing_page),
                        modifier = Modifier
                            .size(24.dp)
                            .padding(bottom = 1.2.dp),
                        contentDescription = ""
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state = state)
                    .padding(padding)
                    .background(MaterialTheme.colorScheme.onPrimary),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LaunchedEffect(srcInputText) {
                    if (stationList.map { it.stationName }
                            .contains(srcInputText) && dstInputText.isEmpty())
                        focusRequesterDst.requestFocus()
                }

                Spacer(modifier = Modifier.height(deviceHeightInDp / 10f))

                AutoCompleteOutLinedTextField(
                    label = stringResource(R.string.chosseSrc),
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
                    onTrashIconClick = {
                        playSound(
                            context = context,
                            soundResourceId = R.raw.remove_sound_effect,
                        )
                        srcInputText = ""
                        startStation = ""
                    }
                )

                Spacer(Modifier.height(deviceHeightInDp / 24))

                AutoCompleteOutLinedTextField(
                    label = stringResource(R.string.chooseDst),
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
                    onTrashIconClick = {
                        playSound(
                            context = context,
                            soundResourceId = R.raw.remove_sound_effect,
                        )
                        dstInputText = ""
                        destStation = ""
                    }
                )

                if (isFindNearestButtonClicked) {
                    ClosestStationsDialogScreen(
                        onDisMiss = { isFindNearestButtonClicked = it },
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

                Spacer(Modifier.height(32.dp))

                ElevatedButton(
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    onClick = {
                        if (srcInputText == dstInputText && (srcInputText.isNotEmpty() && dstInputText.isNotEmpty())) {
                            alertMessageText = " اشتباهی مبدا و مقصد رو یکی زدید "
                            showDialog = true
                        } else {
                            if (stationList.map { it.stationName }
                                    .containsAll(Pair(srcInputText, dstInputText).toList())) {
                                readableFormResultList = BestPathResult(
                                    context,
                                    srcInputText,
                                    dstInputText
                                ).convertPathToReadableForm()
                                navigator.navigate(
                                    PathResultScreenDestination(
                                        startStation = startStation,
                                        destinationStation = destStation
                                    )
                                )
                            } else {
                                alertMessageText = " مبدا و مقصد رو درست وارد کنید "
                                showDialog = true
                            }
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "بهترین مسیرو پیدا کن",
                            color = textColor,
                            style = MaterialTheme.typography.displayMedium
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.Filled.Search, "")
                    }
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
    )
    BackPressAction()
}
