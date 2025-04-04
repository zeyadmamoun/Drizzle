package com.example.drizzle.data

import com.example.drizzle.model.alert.Alert
import com.example.drizzle.model.current.FavoriteWeatherDTO
import com.example.drizzle.model.current.WeatherDTO
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun getAllCurrent(): Flow<List<WeatherDTO>>

    suspend fun deleteAllCurrent()

    suspend fun insertHourForecast(hourForecast: WeatherDTO)

    suspend fun removeCityEntries(cityId: Int)

    suspend fun addCityEntry(entry: FavoriteWeatherDTO)

    fun getCityForecast(cityId: Int): Flow<List<FavoriteWeatherDTO>>

    fun getAllCities(): Flow<List<FavoriteWeatherDTO>>

    suspend fun deleteAlert(alert: Alert)

    suspend fun addAlert(alert: Alert)

    fun getAlerts(): Flow<List<Alert>>
}