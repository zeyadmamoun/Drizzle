package com.example.drizzle.data

import com.example.drizzle.model.current.WeatherDTO
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun getAllCurrent(): Flow<List<WeatherDTO>>

    suspend fun deleteAllCurrent()

    suspend fun insertHourForecast(hourForecast: WeatherDTO)
}