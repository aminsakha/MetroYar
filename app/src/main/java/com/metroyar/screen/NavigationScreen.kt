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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.metroyar.classes.Result
import com.metroyar.utils.log


@Composable
fun NavigationScreen() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        log("into nav",true)
        MyComposable(LocalContext.current)
    }
}

@Composable
fun MyComposable(context: Context) {
    var text1 by remember { mutableStateOf("") }
    var text2 by remember { mutableStateOf("") }
    var list by remember { mutableStateOf(listOf<String>()) }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        text1 = AutoComplete()

        Spacer(Modifier.height(16.dp))

        text2 = AutoComplete()

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            list = Result(
                context,
                text1,
                text2
            ).convertPathToUserUnderstandableForm()
        }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("پیدا کردن بهترین مسیر")
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn {
            items(list) { item ->
                Text(
                    item,
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}