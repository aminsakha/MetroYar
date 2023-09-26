package com.metroyar.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieClipSpec
import com.metroyar.R
import com.metroyar.composable.ShowLottieAnimation
import com.metroyar.db.RealmObject
import com.metroyar.screen.destinations.NavigationBottomDestination
import com.metroyar.ui.theme.line
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun FavoriteStationsScreen(navigator: DestinationsNavigator) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            modifier = Modifier.shadow(8.dp),
            title = {
                Text(text = "ایستگاه های نشان شده ")
            },
            navigationIcon = {
                IconButton(onClick = { navigator.navigate(NavigationBottomDestination()) }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Go back"
                    )
                }
            })
    },
        content = { padding ->
            var dbList by remember {
                mutableStateOf(
                    RealmObject.realmRepo.getListOfFavoriteStations().filter { it != "" })
            }
            if (dbList.isEmpty()) {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ShowLottieAnimation(
                        animationRawId = R.raw.empty,
                        clipSpec = LottieClipSpec.Progress(0.0f, 1f),
                        speed = 2.5f,
                        iterations = 1,
                        onAnimationFinished = {},
                        shouldStopAnimation = false
                    )
                    Text(text = "هنوز ایستگاهی نشان نکردید", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            } else
                LazyColumn(modifier = Modifier.padding(padding)) {
                    items(dbList) { favoriteStationName ->
                        Row(
                            modifier = Modifier
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                modifier = Modifier.weight(0.8f),
                                onClick = {
                                    coroutineScope.launch {
                                        RealmObject.realmRepo.deleteStation(
                                            favoriteStationName
                                        )
                                        dbList =
                                            RealmObject.realmRepo.getListOfFavoriteStations()
                                                .filter { it != "" }
                                    }
                                },
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.icons8_trash_128),
                                    contentDescription = "",
                                    modifier=Modifier.size(24.dp),
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }

                            ElevatedCard(
                                shape = RoundedCornerShape(8.dp),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 4.dp
                                ),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White,
                                ),
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth()
                                    .weight(2f),
                                onClick = { },
                            ) {
                                Text(
                                    text = favoriteStationName,
                                    fontSize = 14.sp,
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(8.dp)
                                )
                            }
                        }
                        Divider(
                            color = line,
                            modifier = Modifier.padding(horizontal = 26.dp),
                            thickness = 0.18.dp
                        )
                    }
                }
        }
    )
}