package com.metroyar.composable


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.metroyar.ui.theme.hint
import com.metroyar.utils.GlobalObjects

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun autoCompleteOutLinedTextField(
    label: String,
    focusRequester: FocusRequester,
    isSrc: Boolean
): String {
    val dropDownStationNamesList = GlobalObjects.stationList.map { it.name }.toSet()

    var inputText by rememberSaveable { mutableStateOf(if (isSrc) GlobalObjects.startStation.value else GlobalObjects.destStation.value) }
    var selectedItem by rememberSaveable { mutableStateOf(if (isSrc) GlobalObjects.startStation.value else GlobalObjects.destStation.value) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    var expanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth()
            .clickable { expanded = false }
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        Column(modifier = Modifier.fillMaxWidth()) {

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .onGloballyPositioned { textFieldSize = it.size.toSize() },
                label = {
                    Text(
                        label,
                        color = hint,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                },
                value = inputText,
                onValueChange = {

                    inputText = it
                    expanded = true
                },
                textStyle = TextStyle(textAlign = TextAlign.End, fontSize = 16.sp),
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = {
                        expanded = !expanded
                        inputText = ""
                        focusRequester.requestFocus()
                        keyboardController?.show()
                    }) {
                        Icon(
                            modifier = Modifier.size(24.dp), imageVector = Icons.Rounded.Clear,
                            contentDescription = "clear", tint = Color.Black
                        )
                    }
                }
            )
            AnimatedVisibility(visible = expanded) {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .width(textFieldSize.width.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .heightIn(max = 150.dp)
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        if (inputText.isNotEmpty()) {
                            items(dropDownStationNamesList.filter { it.contains(inputText) }) { title ->
                                CategoryItems(title = title) {
                                    inputText = it
                                    expanded = false
                                    focusManager.clearFocus()
                                    selectedItem = it
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    return selectedItem
}

@Composable
fun CategoryItems(
    title: String,
    onSelect: (String) -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onSelect(title) }
        .padding(10.dp)) {
        Text(
            text = title,
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End
        )
    }
}