package com.metroyar.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.metroyar.model.NeshanSearchResponseModel
import com.metroyar.utils.log
import kotlinx.serialization.json.Json
import okhttp3.ConnectionPool
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://api.neshan.org/"

val loggingInterceptor = HttpLoggingInterceptor { message ->
    log("http call", message)
}.apply {
    level = HttpLoggingInterceptor.Level.BODY
}

val client: OkHttpClient.Builder =
    OkHttpClient.Builder().protocols(listOf(Protocol.HTTP_1_1)).connectionPool(
        ConnectionPool(0, 5, TimeUnit.MINUTES)
    )
        .connectTimeout(15, TimeUnit.SECONDS) // connect timeout
        .writeTimeout(15, TimeUnit.SECONDS) // write timeout
        .readTimeout(15, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)


private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .client(client.build())
    .build()
private val retrofitWithStringOutPut = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()
interface NeshanApiService {
    @GET("/v1/search")
    suspend fun findNearestStationsFromApi(
        @Header("Api-Key") apiKey: String = "service.66ff4b59db544504899fbf96c9ff0b97",
        @Query("term") term: String,
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double
    ): NeshanSearchResponseModel
}

object MetroYarNeshanApiService {
    val retrofitService: NeshanApiService by lazy {
        retrofit.create(NeshanApiService::class.java)
    }
    val retrofitServiceWithStringOutPut: NeshanApiService by lazy {
        retrofitWithStringOutPut.create(NeshanApiService::class.java)
    }
}