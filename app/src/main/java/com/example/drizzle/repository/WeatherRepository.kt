package com.example.drizzle.repository

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.drizzle.model.current.FavoriteWeatherDTO
import com.example.drizzle.model.current.WeatherDTO
import com.example.drizzle.screens.home.HourForecast
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    // Remote
    suspend fun getCurrentWeather(
        isOnline: Boolean,
        lat: Double,
        lon: Double
    ): WeatherDTO

    suspend fun getHourlyForecast(
        lat: Double,
        lon: Double
    ): List<WeatherDTO>

    // Local cached current weather data
    fun getLocalCurrentWeather(): Flow<List<WeatherDTO>>

    suspend fun deleteLocalCurrentWeather()

    suspend fun insertLocalCurrentEntry(hourForecast: WeatherDTO)

    // Local cached favorite weather data
    suspend fun removeCityEntries(cityId: Int)

    suspend fun addCityEntry(entry: FavoriteWeatherDTO)

    suspend fun getCityForecast(cityId: Int): Flow<List<WeatherDTO>>

    suspend fun getAllCities(): Flow<List<WeatherDTO>>
}