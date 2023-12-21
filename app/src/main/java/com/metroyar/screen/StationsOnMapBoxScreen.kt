package com.metroyar.screen

import android.content.Context
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotationGroup
import com.mapbox.maps.extension.compose.annotation.generated.PolylineAnnotationGroup
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import com.metroyar.R
import com.metroyar.model.MapBoxStation
import com.metroyar.ui.theme.lineFive
import com.metroyar.ui.theme.lineFour
import com.metroyar.ui.theme.lineOne
import com.metroyar.ui.theme.lineSeven
import com.metroyar.ui.theme.lineSix
import com.metroyar.ui.theme.lineThree
import com.metroyar.ui.theme.lineTwo
import com.metroyar.utils.BackPressAction
import com.metroyar.utils.GlobalObjects
import com.metroyar.utils.playSound
import com.metroyar.utils.toastMaker

@OptIn(MapboxExperimental::class)
@Composable
fun MapTest() {
    val context = LocalContext.current
    val mapBoxStationsList = initMapBoxStationsList(context)
    Column(modifier = Modifier.padding(bottom = 6.dp)) {
        MapboxMap(
            modifier = Modifier
                .clip(RoundedCornerShape(bottomEndPercent = 16, bottomStartPercent = 16)),
            mapInitOptionsFactory = { context ->
                MapInitOptions(
                    context = context,
                    styleUri = Style.STANDARD,
                    cameraOptions = CameraOptions.Builder()
                        .center(Point.fromLngLat(51.40515065845713, 35.70122356615692))
                        .zoom(11.8)
                        .build()
                )
            }
        ) {

            val icon =
                AppCompatResources.getDrawable(context, R.drawable.station_on_map_icon)?.toBitmap()

            PolylineAnnotationGroup(
                annotations = listOf(
                    createPolyLine(mapBoxStationsList, 1, lineOne.toArgb()),
                    createPolyLine(mapBoxStationsList, 2, lineTwo.toArgb()),
                    createPolyLine(mapBoxStationsList, 4, lineFour.toArgb()),
                    createPolyLine(mapBoxStationsList, 3, lineThree.toArgb()),
                    createPolyLine(mapBoxStationsList, 7, lineSeven.toArgb()),
                    createPolyLine(mapBoxStationsList, 6, lineSix.toArgb()),
                    createPolyLine(mapBoxStationsList, 5, lineFive.toArgb()),

                    createPolyLine(
                        listOf(
                            mapBoxStationsList.find { it.title == "ایستگاه مترو ارم سبز" || it.title == "ایستگاه مترو علامه جعفری" }!!,
                        ), 4, lineFour.toArgb()
                    ),

                    createPolyLine(
                        mapBoxStationsList.filter {
                            it.title == "ایستگاه مترو پایانه ۱ ۲ فرودگاه مهرآباد" ||
                                    it.title == "ایستگاه مترو پایانه ۴ ۶ فرودگاه مهرآباد" ||
                                    it.title == "ایستگاه مترو بیمه"
                        },
                        4,
                        lineFour.toArgb()
                    ),

                    createPolyLine(
                        mapBoxStationsList.filter {
                            it.title == "ایستگاه مترو پرند" ||
                                    it.title == "ایستگاه مترو فرودگاه امام خمینی" ||
                                    it.title == "ایستگاه مترو نمایشگاه شهر آفتاب" ||
                                    it.title == "ایستگاه مترو شاهد-باقرشهر"
                        }, 1, lineOne.toArgb()
                    ),
                )
            )

            PointAnnotationGroup(
                annotations = mapBoxStationsList.map { Point.fromLngLat(it.x, it.y) }.map {
                    PointAnnotationOptions()
                        .withPoint(it)
                        .withIconImage(icon!!).withIconSize(0.3)
                },
                onClick = { point ->
                    playSound(
                        context = context,
                        soundResourceId = R.raw.change_src_dst,
                        volumeRange = 0.10f
                    )
                    val clickedStation =
                        mapBoxStationsList.find { it.y == point.point.latitude() && it.x == point.point.longitude() }
                    toastMaker(context, clickedStation!!.title)
                    true
                }
            )
        }
    }
    BackPressAction()
}

private fun initMapBoxStationsList(context: Context): List<MapBoxStation> {
    val triplesArray = context.resources.getStringArray(R.array.triples)
    return GlobalObjects.stationList.mapIndexedNotNull { index, station ->
        triplesArray.getOrNull(index)?.let {
            val (y, x, title) = it.split(",")
            MapBoxStation(title, x.toDouble(), y.toDouble(), station.lineNumber)
        }
    }
}

@Composable
private fun createPolyLine(
    mapBoxStationsList: List<MapBoxStation>,
    lineNumber: Int,
    color: Int
): PolylineAnnotationOptions {
    return PolylineAnnotationOptions()
        .withPoints(
            if (mapBoxStationsList.size > 6)
                mapBoxStationsList.filter { it.title != "ایستگاه مترو علامه جعفری" }.dropLast(5)
                    .filter { it.lineNum == lineNumber }.map { Point.fromLngLat(it.x, it.y) }
            else mapBoxStationsList.filter { it.lineNum == lineNumber }
                .map { Point.fromLngLat(it.x, it.y) }
        )
        .withLineColor(color)
        .withLineWidth(8.0).withLineOffset(-2.0)
}
