package com.example.drizzle.data

import com.example.drizzle.model.current.WeatherDTO
import kotlinx.coroutines.flow.Flow

class LocalDataSourceImpl(private val weatherDao: WeatherDao): LocalDataSource {
    override fun getAllCurrent(): Flow<List<WeatherDTO>> {
        return weatherDao.getCurrentForecast()
    }

    override suspend fun deleteAllCurrent() {
        weatherDao.deleteAllCurrent()
    }

    override suspend fun insertHourForecast(hourForecast: WeatherDTO) {
        weatherDao.insertHourForecast(hourForecast)
    }
}