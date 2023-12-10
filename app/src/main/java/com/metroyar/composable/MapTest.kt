package com.metroyar.composable

import android.content.Context
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
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
import com.metroyar.utils.log
import com.ramcosta.composedestinations.annotation.Destination

fun initList(context: Context): MutableList<MapBoxStation> {
    val resultList = mutableListOf<MapBoxStation>()
    val triplesArray = context.resources.getStringArray(R.array.triples)
    GlobalObjects.stationList.forEachIndexed { index, station ->
        val parts = triplesArray[index].split(",")
        val y = parts[0].toDouble()
        val x = parts[1].toDouble()
        resultList.add(MapBoxStation(parts[2], x, y, station.lineNumber))
        log("resut", "${resultList.last().title} -> ${resultList.last().lineNum}")
    }
    return resultList

}

@OptIn(MapboxExperimental::class)
@Composable
fun MapTest() {
    val context = LocalContext.current
    val tmp = initList(context)
    MapboxMap(
        modifier = Modifier.fillMaxSize(),
        mapInitOptionsFactory = { context ->
            MapInitOptions(
                context = context,
                styleUri = Style.STANDARD,
                cameraOptions = CameraOptions.Builder()
                    .center(Point.fromLngLat(51.39117656697642, 35.70093967881435))
                    .zoom(11.5)
                    .build()
            )
        }
    ) {

        val icon =
            AppCompatResources.getDrawable(context, R.drawable.station_on_map_icon)?.toBitmap()

        PolylineAnnotationGroup(
            annotations = listOf(
                createPolyLine(tmp, 1, lineOne.toArgb()),
                createPolyLine(tmp, 7, lineSeven.toArgb()),
                createPolyLine(tmp, 6, lineSix.toArgb()),
                createPolyLine(tmp, 5, lineFive.toArgb()),
                createPolyLine(
                    listOf(
                        tmp.find { it.title == "ایستگاه مترو ارم سبز" }!!,
                        tmp.find { it.title == "ایستگاه مترو علامه جعفری" }!!
                    ), 4, lineFour.toArgb()
                ),
                createPolyLine(tmp, 4, lineFour.toArgb()),
                createPolyLine(tmp, 3, lineThree.toArgb()),
                createPolyLine(
                    listOf(
                        tmp.find { it.title == "ایستگاه مترو پایانه ۱ ۲ فرودگاه مهرآباد" }!!,
                        tmp.find { it.title == "ایستگاه مترو پایانه ۴ ۶ فرودگاه مهرآباد" }!!,
                        tmp.find { it.title == "ایستگاه مترو بیمه" }!!
                    ), 4, lineFour.toArgb()
                ),
                createPolyLine(tmp, 2, lineTwo.toArgb()),
                createPolyLine(
                    listOf(
                        tmp.find { it.title == "ایستگاه مترو پرند" }!!,
                        tmp.find { it.title == "ایستگاه مترو فرودگاه امام خمینی" }!!,
                        tmp.find { it.title == "ایستگاه مترو نمایشگاه شهر آفتاب" }!!,
                        tmp.find { it.title == "ایستگاه مترو شاهد-باقرشهر" }!!
                    ), 1, lineOne.toArgb()
                ),
            )
        )

        PointAnnotationGroup(
            annotations = tmp.map { Point.fromLngLat(it.x, it.y) }.map {
                PointAnnotationOptions()
                    .withPoint(it)
                    .withIconImage(icon!!).withIconSize(0.3)
            },
            onClick = { point ->
                val find =
                    tmp.find { it.y == point.point.latitude() && it.x == point.point.longitude() }

                Toast.makeText(
                    context,
                    find?.title,
                    Toast.LENGTH_SHORT
                ).show()
                true
            }
        )
    }
    BackPressAction()
}

@Composable
private fun createPolyLine(
    tmp: List<MapBoxStation>,
    lineNumber: Int,
    color: Int
): PolylineAnnotationOptions {

    return PolylineAnnotationOptions()
        .withPoints(
            if (tmp.size > 6)
                tmp.filter { it.title != "ایستگاه مترو علامه جعفری" }.dropLast(5)
                    .filter { it.lineNum == lineNumber }.map { Point.fromLngLat(it.x, it.y) }
            else tmp.filter { it.lineNum == lineNumber }.map { Point.fromLngLat(it.x, it.y) }
        )
        .withLineColor(color)
        .withLineWidth(8.0).withLineOffset(-2.0)
}
