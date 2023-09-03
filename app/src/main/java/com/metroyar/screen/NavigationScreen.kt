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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.metroyar.utils.GlobalObjects.resultList
import com.metroyar.utils.GlobalObjects.startStation
import com.metroyar.utils.GlobalObjects.stationList
import com.metroyar.utils.Test2

@Composable
fun NavigationScreen() {
    NavigatingScreen(LocalContext.current)
}

@Composable
fun NavigatingScreen(context: Context) {
    var srcInputText by remember { mutableStateOf(startStation) }
    var dstInputText by remember { mutableStateOf(destStation) }

    var showDialog by remember { mutableStateOf(false) }
    var isFindNearestButtonClicked by remember { mutableStateOf(false) }
    val focusRequesterDst = remember { FocusRequester() }
    val focusRequesterSrc = remember { FocusRequester() }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (stationList.map { it.name }.contains(srcInputText) && dstInputText.isEmpty())
            if (focusRequesterDst != null)
                focusRequesterDst.requestFocus()

        autoCompleteOutLinedTextField(
            label = "ایستگاه مبدا رو انتخاب کن",
            focusRequester = focusRequesterSrc,
            value = srcInputText,
            onValueChange = {
                srcInputText = it
            },
            onItemSelectedChange = {
                srcInputText = it
                startStation = srcInputText
            },
            onTrashIconClick = { srcInputText = "" }
        )

        Spacer(Modifier.height(16.dp))

        autoCompleteOutLinedTextField(
            label = "ایستگاه مقصد رو انتخاب کن",
            focusRequester = focusRequesterDst,
            value = dstInputText,
            onValueChange = {
                dstInputText = it
            },
            onItemSelectedChange = {
                dstInputText = it
                destStation = dstInputText
            },
            onTrashIconClick = { dstInputText = "" }
        )

        Spacer(Modifier.height(16.dp))
        if (isFindNearestButtonClicked) {
            Test2(
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
                if (srcInputText == dstInputText ||
                    srcInputText.isEmpty() != dstInputText.isEmpty()
                )
                    showDialog = true
                else
                    resultList.value = Result(
                        context,
                        srcInputText,
                        dstInputText
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