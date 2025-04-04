package com.example.drizzle.repository

import com.example.drizzle.data.LocalDataSource
import com.example.drizzle.model.alert.Alert
import com.example.drizzle.model.current.FavoriteWeatherDTO
import com.example.drizzle.model.current.WeatherDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLocalDataSource(
    private val alerts: MutableList<Alert> = mutableListOf(),
    private val weatherDTOList: MutableList<WeatherDTO> = mutableListOf(),
    private val favoriteList: MutableList<FavoriteWeatherDTO> = mutableListOf()
) : LocalDataSource {
    override fun getAllCurrent(): Flow<List<WeatherDTO>> = flow {
        emit(weatherDTOList)
    }

    override suspend fun deleteAllCurrent() {
        weatherDTOList.clear()
    }

    override suspend fun insertHourForecast(hourForecast: WeatherDTO) {
        weatherDTOList.add(hourForecast)
    }

    override suspend fun removeCityEntries(cityId: Int) {
        weatherDTOList.forEach {
            if (it.cityId == cityId)
                weatherDTOList.remove(it)
        }
    }

    override suspend fun addCityEntry(entry: FavoriteWeatherDTO) {
        favoriteList.add(entry)
    }

    override fun getCityForecast(cityId: Int): Flow<List<FavoriteWeatherDTO>> = flow {
        val result = favoriteList.filter { it.cityId == cityId }
        emit(result)
    }

    override fun getAllCities(): Flow<List<FavoriteWeatherDTO>> = flow {
        emit(favoriteList)
    }

    override suspend fun deleteAlert(alert: Alert) {
        alerts.remove(alert)
    }

    override suspend fun addAlert(alert: Alert) {
        alerts.add(alert)
    }

    override fun getAlerts(): Flow<List<Alert>> = flow {
        emit(alerts)
    }
}