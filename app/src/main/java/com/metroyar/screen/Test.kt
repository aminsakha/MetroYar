package com.metroyar.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.metroyar.utils.GlobalObjects
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AutoComplete() {
    val dropDownStationNamesList = GlobalObjects.stationList.map { it.name }

    var category by remember { mutableStateOf("") }
    val heightTextFields by remember { mutableStateOf(55.dp) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    var expanded by remember { mutableStateOf(false) }
    var enabled by remember { mutableStateOf(true) }
    var shouldFocuse by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }

    // Category Field
    Column(
        modifier = Modifier
            .padding(30.dp)
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    expanded = false
                }
            )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            val scope = rememberCoroutineScope()
            LaunchedEffect(shouldFocuse) {
                if (shouldFocuse) {
                    focusRequester.requestFocus()
                } else
                    focusRequester.freeFocus()
            }
            OutlinedTextField(
                //enabled = enabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(heightTextFields)
                    .focusRequester(focusRequester)
                    .onGloballyPositioned { coordinates ->
                        textFieldSize = coordinates.size.toSize()
                    },
                label = {
                    Text(
                        "تست", modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                },
                value = category,
                onValueChange = {
                    category = it
                    expanded = true
                },
                textStyle =
                TextStyle(
                    textAlign = TextAlign.End,
                    fontSize = 16.sp
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = {
                        expanded = !expanded
                        category = ""
                        enabled = false
                        scope.launch {
                            delay(1)
                            enabled = true
                            shouldFocuse = false
                        }
                    }) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Rounded.Clear,
                            contentDescription = "clear",
                            tint = Color.Black
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
                            .background(MaterialTheme.colorScheme.secondaryContainer),
                    ) {
                        if (category.isNotEmpty()) {
                            items(
                                dropDownStationNamesList.filter {
                                    it.contains(category)
                                }
                            ) {
                                CategoryItems(title = it) { title ->
                                    category = title
                                    expanded = false
                                    enabled = false
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
fun CategoryItems(
    title: String,
    onSelect: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onSelect(title)
            }

            .padding(10.dp)
    ) {
        Text(
            text = title, fontSize = 16.sp, modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End
        )
    }
}