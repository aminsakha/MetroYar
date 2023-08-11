package com.metroyar.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.PopupProperties
import com.metroyar.classes.Result

@Composable
fun NavigationScreen() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AutoComplete()
//        Text(
//            text =
//            Result(
//                LocalContext.current,
//                "ایران خودرو",
//                "خیام"
//            ).convertPathToUserUnderstandableForm()
//                .toString(),
//            textAlign = TextAlign.End,
//            fontSize = 24.sp
//        )
    }
}

@Composable
fun MyComposable(onListGenerated: (List<String>) -> Unit) {
    var text1 by remember { mutableStateOf("") }
    var text2 by remember { mutableStateOf("") }
    var list by remember { mutableStateOf(listOf<String>()) }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        OutlinedTextField(
            value = text1,
            onValueChange = { text1 = it },
            label = { Text("Text Field 1") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = text2,
            onValueChange = { text2 = it },
            label = { Text("Text Field 2") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            list = listOf("Item 1", "Item 2", "Item 3")
            onListGenerated(list)
        }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Generate List")
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn {
            items(list) { item ->
                Text(item, Modifier.padding(16.dp))
            }
        }
    }
}

@Composable
fun DropDownMenu(optionList: List<String>, label: String) {
    var expanded by remember { mutableStateOf(false) }

    var selectedText by remember { mutableStateOf("") }

    var textfieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown


    Column() {
        OutlinedTextField(
            value = selectedText,
            onValueChange = { selectedText = it },
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textfieldSize = coordinates.size.toSize()
                }
                .clickable { expanded = !expanded },
            label = { Text(label) },
            trailingIcon = {
                Icon(icon, "Drop Down Icon",
                    Modifier.clickable { expanded = !expanded })
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textfieldSize.width.toDp() }),
            properties = PopupProperties(focusable = false)
        ) {
            optionList.forEach { label ->
                DropdownMenuItem(onClick = {
                    selectedText = label
                    expanded = !expanded
                }, text = { Text(text = label) })
            }
        }
    }
}