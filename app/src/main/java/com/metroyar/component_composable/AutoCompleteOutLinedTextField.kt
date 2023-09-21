package com.metroyar.component_composable


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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.metroyar.R
import com.metroyar.db.RealmObject.realmRepo
import com.metroyar.ui.theme.hint
import com.metroyar.ui.theme.line
import com.metroyar.ui.theme.textColor
import com.metroyar.ui.theme.turnedOff2
import com.metroyar.utils.GlobalObjects.deviceHeightInDp
import com.metroyar.utils.GlobalObjects.stationList
import kotlinx.coroutines.launch

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
    val dataBaseList = realmRepo.getListOfFavoriteStations()
    val dropDownStationNamesList =
        stationList.map { it.stationName }.toSet().sorted()
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    var expanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier.padding(horizontal = 24.dp)
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .clickable { expanded = false }
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        Column(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused && inputValue.isEmpty()) {
                            expanded = true
                        }
                    }
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

                    if (inputValue.isEmpty() && dataBaseList.isNotEmpty())
                        expanded = true
                    if (!expanded)
                        keyboardController?.hide()
                },
                textStyle = TextStyle( fontSize = 16.sp,textDirection = TextDirection.Content, color = textColor),
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
                    modifier = Modifier.padding(horizontal = 5.dp),
                    shape = RoundedCornerShape(
                        bottomStart = 8.dp,
                        bottomEnd = 8.dp
                    )
                )
                {
                    LazyColumn(
                        modifier = Modifier
                            .heightIn(max = deviceHeightInDp/4.5f)
                            .background(MaterialTheme.colorScheme.onSecondary)
                    ) {
                        val filteredList =
                            if (inputValue.isEmpty() && dataBaseList.isNotEmpty())
                                dropDownStationNamesList.sortedWith(compareBy { element ->
                                    dataBaseList.indexOf(element)
                                }).reversed()
                            else {
                                dropDownStationNamesList.filter {
                                    it.contains(inputValue)
                                }.reversed()
                            }
                        items(filteredList) { stationName ->
                            DropDownStationSuggestionItem(
                                onStarSelected = {
                                    coroutineScope.launch {
                                        if (it)
                                            realmRepo.deleteStation(stationName)
                                        else
                                            realmRepo.insertStation(stationName = stationName)
                                    }
                                },
                                itemName = stationName,
                                onItemSelected = {
                                    onItemSelectedChange.invoke(stationName)
                                    expanded = false
                                    focusManager.clearFocus()
                                }
                            )
                            Divider(
                                color = line,
                                modifier = Modifier.padding(horizontal = 16.dp),
                                thickness = 0.18.dp
                            )
                        }
                    }
                }
            }
        }
    }
}
