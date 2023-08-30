package com.metroyar.screen

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.metroyar.R
import com.metroyar.classes.Result
import com.metroyar.composable.OneBtnAlertDialog
import com.metroyar.composable.autoCompleteOutLinedTextField
import com.metroyar.ui.theme.line
import com.metroyar.utils.GlobalObjects.destStation
import com.metroyar.utils.GlobalObjects.locationFlow
import com.metroyar.utils.GlobalObjects.resultList
import com.metroyar.utils.GlobalObjects.startStation
import com.metroyar.utils.getCurrentLocation
import com.metroyar.utils.isGpsEnabled
import com.metroyar.utils.log
import com.metroyar.utils.setTextFieldsWithApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun NavigationScreen() {
    NavigatingScreen(LocalContext.current)
}

@Composable
fun NavigatingScreen(context: Context) {
    var showDialog by remember { mutableStateOf(false) }
    var isFindNearestButtonClicked by remember { mutableStateOf(false) }
    val focusRequesterDst by remember { mutableStateOf(FocusRequester()) }
    val focusRequesterSrc by remember { mutableStateOf(FocusRequester()) }
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LaunchedEffect(key1 = startStation.value) {
            if (startStation.value.isNotEmpty() && destStation.value.isEmpty())
                focusRequesterDst.requestFocus()
        }

        startStation.value =
            autoCompleteOutLinedTextField(
                label = "ایستگاه مبدا رو انتخاب کن",
                focusRequesterSrc, true
            )

        Spacer(Modifier.height(16.dp))

        destStation.value = autoCompleteOutLinedTextField(
            label = "ایستگاه مقصد رو انتخاب کن",
            focusRequesterDst, false
        )

        Spacer(Modifier.height(16.dp))
        if (isFindNearestButtonClicked) {
            PermissionScreen(onPermissionGranted = {
                if (isGpsEnabled(context)) {

                    coroutineScope.launch {
                        withContext(Dispatchers.Main) {
                            getCurrentLocation(context)
                        }
                        withContext(Dispatchers.IO) {
                            locationFlow.collect { location ->
                                if (location != null) {
                                    log("res 22 ", location.y)
                                    setTextFieldsWithApiResponse()
                                }
                            }
                        }
                    }
                }
            }, onPermissionGrantedNextScreen = { if (!isGpsEnabled(context)) Layout() })
        }

        Button(onClick = {
            isFindNearestButtonClicked = true
        }) {
            Text(text = "نزدیکترین ایستگاه به من")
        }
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                if (startStation.value == destStation.value ||
                    startStation.value.isEmpty() != destStation.value.isEmpty()
                )
                    showDialog = true
                else
                    resultList.value = Result(
                        context,
                        startStation.value,
                        destStation.value
                    ).convertPathToUserUnderstandableForm()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("برام بهترین مسیرو پیدا کن", color = MaterialTheme.colorScheme.onPrimary)
        }
        OneBtnAlertDialog(
            visible = showDialog,
            onConfirm = { showDialog = false },
            onDismissRequest = { showDialog = false },
            title = stringResource(R.string.notice_text),
            message = stringResource(R.string.same_input_output_message),
            okMessage = stringResource(R.string.ok_message)
        )
        Spacer(Modifier.height(12.dp))

        LazyColumn {
            itemsIndexed(resultList.value) { index, item ->
                Text(
                    item,
                    Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.End
                )
                if (index < resultList.value.lastIndex)
                    Divider(color = line, thickness = 1.dp)
            }
        }
    }
}