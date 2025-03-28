package com.example.drizzle.network

import com.example.drizzle.model.current.CurrentWeatherResponse
import com.example.drizzle.model.hourly.HourlyWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("/data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") tempUnit: String = "metric"
    ): CurrentWeatherResponse

    @GET("/data/2.5/forecast")
    suspend fun getHourlyWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") tempUnit: String = "metric"
    ): HourlyWeatherResponse
}