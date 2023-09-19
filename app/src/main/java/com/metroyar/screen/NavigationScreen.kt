package com.metroyar.screen

import android.content.Context
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.material3.BadgeDefaults.containerColor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.metroyar.R
import com.metroyar.classes.BestPathResult
import com.metroyar.component_composable.AutoCompleteOutLinedTextField
import com.metroyar.component_composable.ShouldConfirmAlertDialog
import com.metroyar.screen.destinations.PathResultScreenDestination
import com.metroyar.utils.GlobalObjects.destStation
import com.metroyar.utils.GlobalObjects.deviceHeightInDp
import com.metroyar.utils.GlobalObjects.resultList
import com.metroyar.utils.GlobalObjects.startStation
import com.metroyar.utils.GlobalObjects.stationList
import com.metroyar.utils.SuggestionStationsLayout
import com.metroyar.utils.log
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationScreen(context: Context, navigator: DestinationsNavigator) {
    var srcInputText by remember { mutableStateOf(startStation) }
    var dstInputText by remember { mutableStateOf(destStation) }
    var alertMessageText by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var isFindNearestButtonClicked by remember { mutableStateOf(false) }
    val focusRequesterDst = remember { FocusRequester() }
    val focusRequesterSrc = remember { FocusRequester() }

    Scaffold(floatingActionButton = {
        FloatingActionButton(
            shape = FloatingActionButtonDefaults.largeShape,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.padding(bottom = 12.dp, end = 8.dp),
            onClick = { isFindNearestButtonClicked = true },
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_my_location_24),
                ""
            )
        }
    },
        floatingActionButtonPosition = FabPosition.End,
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LaunchedEffect(srcInputText) {
                    if (stationList.map { it.stationName }
                            .contains(srcInputText) && dstInputText.isEmpty())
                        focusRequesterDst.requestFocus()
                }

                Spacer(modifier = Modifier.height(deviceHeightInDp / 7))

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
                    onTrashIconClick = { srcInputText = "" }
                )

                Spacer(Modifier.height(32.dp))

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
                    onTrashIconClick = { dstInputText = "" }
                )

                Spacer(Modifier.height(16.dp))
                if (isFindNearestButtonClicked) {
                    SuggestionStationsLayout(
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

                Spacer(Modifier.height(16.dp))

                ElevatedButton(
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
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
                                navigator.navigate(
                                    PathResultScreenDestination(
                                        startStation = startStation,
                                        destinationStation = destStation
                                    )
                                )
                            } else {
                                alertMessageText = " مبدا و مقصد رو درست بزن لطفا "
                                showDialog = true
                            }
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        "بهترین مسیرو پیدا کن",
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(Icons.Filled.Search, "")

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
}
