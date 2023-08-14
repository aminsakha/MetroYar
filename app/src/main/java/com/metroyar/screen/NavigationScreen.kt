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
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.metroyar.composable.autoCompleteOutLinedTextField
import com.metroyar.ui.theme.line


@Composable
fun NavigationScreen() {
    MyComposable(LocalContext.current)
}

@Composable
fun MyComposable(context: Context) {
    var startStation by remember { mutableStateOf("") }
    var destStation by remember { mutableStateOf("") }
    var resultList by remember { mutableStateOf(listOf<String>()) }
  LaunchedEffect(key1 = startStation){
   //   if (startStation!="")

  }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        startStation = autoCompleteOutLinedTextField(label = "ایستگاه مبدا")

        Spacer(Modifier.height(16.dp))

        destStation = autoCompleteOutLinedTextField(label = "ایستگاه مقصد")

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            resultList = Result(
                context,
                startStation,
                destStation
            ).convertPathToUserUnderstandableForm()
        }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("پیدا کردن بهترین مسیر")
        }

        Spacer(Modifier.height(12.dp))

        LazyColumn {
            itemsIndexed(resultList) { index, item ->
                Text(
                    item,
                    Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.End
                )
                if (index < resultList.lastIndex)
                    Divider(color = line, thickness = 1.dp)
            }
        }
    }
}