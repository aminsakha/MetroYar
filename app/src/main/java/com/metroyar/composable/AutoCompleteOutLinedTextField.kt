package com.metroyar.composable


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.metroyar.R
import com.metroyar.ui.theme.hint
import com.metroyar.utils.GlobalObjects.stationList

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AutoCompleteOutLinedTextField(
    inputValue: String,
    onInputValueChange: (String) -> Unit,
    onItemSelectedChange: (String) -> Unit,
    onTrashIconClick: (String) -> Unit,
    label: String,
    focusRequester: FocusRequester,
) {
    val dropDownStationNamesList = stationList.map { it.stationName }.toSet().sorted()
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    var expanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth()
            .focusRequester(focusRequester)
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
                shape = RoundedCornerShape(8.dp),
                value = inputValue,
                onValueChange = {
                    onInputValueChange.invoke(it)
                    expanded = dropDownStationNamesList.binarySearch(it) < 0
                    if (!expanded)
                        keyboardController?.hide()
                },
                textStyle = TextStyle(textAlign = TextAlign.End, fontSize = 16.sp),
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = {
                        expanded = !expanded
                        onTrashIconClick.invoke("")
                        focusRequester.requestFocus()
                        keyboardController?.show()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.icons8_trash_128),
                            contentDescription = "clear",
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
                        if (inputValue.isNotEmpty()) {
                            items(dropDownStationNamesList.filter { it.contains(inputValue) }) { title ->
                                DropDownStationSuggestionItem(itemName = title) {
                                    onItemSelectedChange.invoke(title)
                                    expanded = false
                                    focusManager.clearFocus()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DropDownStationSuggestionItem(
    itemName: String,
    onItemSelected: (String) -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onItemSelected(itemName) }
        .padding(10.dp)) {
        Text(
            text = itemName,
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End
        )
    }
}