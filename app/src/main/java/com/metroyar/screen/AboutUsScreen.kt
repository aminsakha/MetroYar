package com.metroyar.screen

import android.text.BidiFormatter
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metroyar.R
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun AboutUsScreen(navigator: DestinationsNavigator) {
    val persianFormatter = BidiFormatter.getInstance(Locale("fa"))
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            modifier = Modifier.shadow(8.dp),
            title = {
                Text(text = "درباره ما")
            },
            navigationIcon = {
                IconButton(onClick = { navigator.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Go back"
                    )
                }
            })
    },
        content = { padding ->
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_main),
                        contentDescription = "",
                        modifier = Modifier.clip(
                            CircleShape
                        )
                    )
                    Text(
                        text = "مترو یار",
                        style = MaterialTheme.typography.displayMedium,
                        fontSize = 24.sp
                    )
                    Text(
                        text = "یار مترویی شما",
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.displayMedium,
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    modifier=Modifier.padding(horizontal = 8.dp),
                    textAlign = TextAlign.End,
                    fontSize = 16.sp,
                    text = "اپلیکیشن مترو یار با هدف تسهیل رفت آمد هم وطنان عزیزمان در متروی تهران و حومه راه اندازی شده است ${persianFormatter.unicodeWrap(".")} ",
                )
                Text(
                    fontSize = 16.sp,
                    modifier=Modifier.padding(horizontal = 8.dp),
                    textAlign = TextAlign.End,
                    text = "در ادامه به قابلیت های کلیدی این برنامه اشاره خواهد شد ${persianFormatter.unicodeWrap(":")} ",
                )
                Spacer(modifier = Modifier.height(8.dp))
                titleAndDsc(title = " مسیریابی بین دو ایستگاه مترو  • \t ", dsc = "مترو یار با دریافت ایستگاه مبدا و ایستگاه مقصد از سمت کاربر بهترین مسیر ممکن رو با درنظر گرفتن اینکه مسیر پیشنهادی دارای کمترین تعویض خط و کوتاه ترین زمان رسیدن باشد را به کاربر نشان خواهد داد")
                Spacer(modifier = Modifier.height(6.dp))
                titleAndDsc(
                    title = "اطلاعات زمانی مهم  • \t",
                    dsc = "مترو یار پس از نشان دادن بهترین مسیر به کاربر همچنین کل زمانی که شما در مترو خواهید بود تا به مقصد خود برسید را محاسبه خواهد کرد و در کنار زمان رسیدن متروی بعدی در مبدا به شما نشان خواهد داد  "
                )
                Spacer(modifier = Modifier.height(6.dp))
                titleAndDsc(
                    title = "نشان کردن ایستگاه ها  • \t",
                    dsc = "با نشان کردن هر ایستگاه میتوانید در دفعات بعدی ای که برای مسیریابی به مترویار مراجعه میکنید ، قادر خواهید بود آن ایستگاه ها را در صدر منویی که برای شما باز میشود تا ایستگاه خود را برای مبدا یا مقصد انتخاب کنید مشاهده کنید"
                )
                Spacer(modifier = Modifier.height(6.dp))
                titleAndDsc(
                    title = "یافتن ایستگاه های نزدیک  • \t",
                    dsc = "از طریق این قابلیت شما قادر خواهید بود که ایستگاه های نزدیک خودتان را در هرجایی از تهران و یا حومه تهران که هستید مشاهده کنید و آن را به عنوان مبدا یا مقصد خود جهت مسیریابی انتخاب کنید"
                )
                Spacer(modifier = Modifier.height(6.dp))
                titleAndDsc(
                    title = "بروز ترین نسخه نقشه مترو  • \t",
                    dsc = "مترویار این تضمین را میدهد که با بروزرسانی های متعدد همواره در منوی خود بروزترین نسخه نقشه مترو تهران و حومه را در اختیار هم وطنان عزیزمان قرار بدهد"
                )
                Spacer(modifier = Modifier.height(6.dp))
                titleAndDsc(
                    title = "اشتراک گذاری مسیر  • \t",
                    dsc = "در صفحه ای که نتیجه مسیریابی خود را مشاهده میکنید قابلیتی برای اشتراک گذاری مسیر از طریق شبکه های اجتماعی و هر برنامه ای که قابلیت اشتراک گذاری فایل دارد تعبیه شده است تا در یک عکس تروتمیز بتوانید مسیر را به اشتراک بگذارید"
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier=Modifier.padding(horizontal = 8.dp),
                    textAlign = TextAlign.End,
                    fontSize = 16.sp,
                    text = "در آخر، خوشحال میشیم نظراتتون رو جهت بهبود هرچه بهتر اپلیکیشن و رفع مشکلات احتمالی از طریق بخش گزارش خطا با ما به اشتراک بگذارید ${persianFormatter.unicodeWrap(".")} ",
                )
            }
        }
    )
}

@Composable
private fun titleAndDsc(title: String, dsc: String) {
        Text(modifier = Modifier.padding(horizontal = 4.dp),text = title, fontSize = 17.sp, style = MaterialTheme.typography.displayMedium, textAlign = TextAlign.End)
        Spacer(modifier = Modifier.height(2.dp))
        Text(modifier = Modifier.padding(horizontal = 8.dp),text = dsc, fontSize = 16.sp, textAlign = TextAlign.End)
}