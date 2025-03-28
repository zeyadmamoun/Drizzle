package com.example.drizzle.repository

import com.example.drizzle.model.current.CurrentWeatherResponse
import com.example.drizzle.model.current.WeatherDTO
import com.example.drizzle.model.hourly.HourlyWeatherResponse

interface WeatherRepository {
    suspend fun getCurrentWeather(
        isOnline: Boolean,
        lat: Double,
        lon: Double
    ): WeatherDTO

    suspend fun getHourlyWeather(
        lat: Double,
        lon: Double
    ): List<WeatherDTO>
}