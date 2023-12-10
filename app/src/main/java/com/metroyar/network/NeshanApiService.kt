package com.metroyar.network

import android.util.Log
import androidx.annotation.Keep
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.metroyar.model.NeshanSearchPlaceResponseModel
import com.metroyar.utils.GlobalObjects
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.ConnectionPool
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://api.neshan.org/"

val loggingInterceptor = HttpLoggingInterceptor { message ->
    Log.i(GlobalObjects.TAG, message)
}.apply {
    level = HttpLoggingInterceptor.Level.BODY
}

val client: OkHttpClient.Builder =
    OkHttpClient.Builder().protocols(listOf(Protocol.HTTP_1_1)).connectionPool(
        ConnectionPool(0, 5, TimeUnit.MINUTES)
    )
        .connectTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)

@OptIn(ExperimentalSerializationApi::class)
private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .client(client.build())
    .build()

@Keep interface NeshanApiService {
    @GET("/v1/search")
    @Keep suspend fun findNearestStationsFromApi(
        @Header("Api-Key") apiKey: String = "service.66ff4b59db544504899fbf96c9ff0b97",
        @Query("term") term: String,
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double
    ): NeshanSearchPlaceResponseModel
}

object MetroYarNeshanApiService {
    val retrofitService: NeshanApiService by lazy {
        retrofit.create(NeshanApiService::class.java)
    }
}