package com.metroyar.composable

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotationGroup
import com.mapbox.maps.extension.style.expressions.dsl.generated.literal
import com.mapbox.maps.extension.style.expressions.generated.Expression
import com.mapbox.maps.plugin.annotation.AnnotationConfig
import com.mapbox.maps.plugin.annotation.AnnotationSourceOptions
import com.mapbox.maps.plugin.annotation.ClusterOptions
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.metroyar.R
import com.ramcosta.composedestinations.annotation.Destination


@OptIn(MapboxExperimental::class)
@Destination
@Composable
fun MapTest() {
    val context = LocalContext.current
    MapboxMap(
        modifier = Modifier.fillMaxSize(),
        mapInitOptionsFactory = { context ->
            MapInitOptions(
                context = context,
                styleUri = Style.TRAFFIC_DAY,
                cameraOptions = CameraOptions.Builder()
                    .center(Point.fromLngLat(51.388740, 35.714045))
                    .zoom(16.0)
                    .build()
            )
        }
    ) {

        val a = Point.fromLngLat(51.388740, 35.714045)
        val b = Point.fromLngLat(51.389963, 35.710317)
        val icon = context.getDrawable(R.drawable.train)?.toBitmap()
        val POINTS_TO_ADD = listOf<Point>(a, b)
        PointAnnotationGroup(
            annotations = POINTS_TO_ADD.map {
                PointAnnotationOptions()
                    .withPoint(it)
                    .withIconImage(icon!!).withIconSize(3.0)
            },
            annotationConfig = AnnotationConfig(
                annotationSourceOptions = AnnotationSourceOptions(
                    clusterOptions = ClusterOptions(
                        textColorExpression = Expression.color(Color.YELLOW),
                        textColor = Color.BLACK,
                        textSize = 20.0,
                        circleRadiusExpression = literal(25.0),
                        colorLevels = listOf(
                            Pair(100, Color.RED),
                            Pair(50, Color.BLUE),
                            Pair(0, Color.GREEN)
                        )
                    )
                )
            ),
            onClick = {
                Toast.makeText(
                    context,
                    "Clicked on Point Annotation Cluster: $it",
                    Toast.LENGTH_SHORT
                ).show()
                true
            }
        )

    }
}