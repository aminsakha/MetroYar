package com.metroyar.composable


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.metroyar.R
import com.metroyar.db.RealmObject.realmRepo
import com.metroyar.ui.theme.*
import com.metroyar.utils.GlobalObjects.deviceHeightInDp
import com.metroyar.utils.GlobalObjects.stationList
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AutoCompleteOutLinedTextField(
    inputValue: String,
    hasFoc: (Boolean) -> Unit,
    secondCheckExpand: Boolean,
    onInputValueChange: (String) -> Unit,
    onItemSelectedChange: (String) -> Unit,
    onTrashIconClick: (String) -> Unit,
    label: String,
    focusRequester: FocusRequester,
) {
    val dataBaseFavoriteStations = realmRepo.getListOfFavoriteStations()
    val dropDownStationNamesList =
        stationList.map { it.stationName }.toSet().shuffled()
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    var expanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
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
                        if (focusState.isFocused)
                            hasFoc.invoke(true)
                        if (focusState.isFocused && inputValue.isEmpty()) {
                            expanded = true
                        }
                    }
                    .focusRequester(focusRequester)
                    .onGloballyPositioned { textFieldSize = it.size.toSize() },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        expanded = !expanded
                        focusManager.clearFocus()
                    }
                ),
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
                    if (inputValue.isEmpty() && dataBaseFavoriteStations.isNotEmpty())
                        expanded = true
                    if (!expanded)
                        keyboardController?.hide()
                },
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.iran_mobile)),
                    textDirection = TextDirection.Content,
                    color = textColor
                ),
                singleLine = true,
                leadingIcon = {
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
            AnimatedVisibility(visible = expanded && secondCheckExpand) {
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
                            .heightIn(max = deviceHeightInDp / 5.64f)
                            .background(MaterialTheme.colorScheme.onSecondary)
                    ) {
                        val filteredList =
                            if (inputValue.isEmpty() && dataBaseFavoriteStations.isNotEmpty())
                                dropDownStationNamesList.sortedWith(compareBy { element ->
                                    dataBaseFavoriteStations.indexOf(element)
                                }).reversed()
                            else {
                                dropDownStationNamesList.filter {
                                    it.contains(inputValue)
                                }.sortedWith(compareByDescending {
                                    it.startsWith(inputValue)
                                })
                            }
                        items(filteredList) { stationName ->
                            DropDownStationItem(
                                onStarSelected = {
                                    coroutineScope.launch {
                                        if (it)
                                            realmRepo.deleteStation(stationName)
                                        else
                                            realmRepo.insertStation(station = stationName)
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