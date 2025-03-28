package com.example.drizzle.network

import com.example.drizzle.model.current.CurrentWeatherResponse
import com.example.drizzle.model.hourly.HourlyWeatherResponse
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RemoteDataSource {
    private const val API_KEY = "a57d8ec0ac57b2fa3277db7302d85277"

    private const val BASE_URL = "https://pro.openweathermap.org"

    private val json = Json {
        ignoreUnknownKeys = true // Ignores extra JSON fields
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(
            json.asConverterFactory(
                "application/json; charset=UTF8".toMediaType()
            )
        ).build()

    private val retrofitService: WeatherService by lazy {
        retrofit.create(WeatherService::class.java)
    }

    suspend fun getCurrentWeather(lat: Double, long: Double): CurrentWeatherResponse {
        return retrofitService.getCurrentWeather(lat, long, API_KEY)
    }

    suspend fun getHourlyWeather(lat: Double, long: Double): HourlyWeatherResponse{
        return retrofitService.getHourlyWeather(lat, long, API_KEY)
    }
}