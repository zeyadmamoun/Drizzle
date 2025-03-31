package com.example.drizzle.repository

import com.example.drizzle.model.current.WeatherDTO
import com.example.drizzle.screens.home.HourForecast
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getCurrentWeather(
        isOnline: Boolean,
        lat: Double,
        lon: Double
    ): WeatherDTO

    suspend fun getHourlyForecast(
        lat: Double,
        lon: Double
    ): List<WeatherDTO>

    fun getLocalCurrentWeather(): Flow<List<WeatherDTO>>

    suspend fun deleteLocalCurrentWeather()

    suspend fun insertLocalCurrentEntry(hourForecast: WeatherDTO)
}