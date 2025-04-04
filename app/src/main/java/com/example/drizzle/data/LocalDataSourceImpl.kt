package com.example.drizzle.data

import com.example.drizzle.model.alert.Alert
import com.example.drizzle.model.current.FavoriteWeatherDTO
import com.example.drizzle.model.current.WeatherDTO
import kotlinx.coroutines.flow.Flow

class LocalDataSourceImpl(private val weatherDao: WeatherDao): LocalDataSource {
    // here all functions that work on the cached current weather data
    override fun getAllCurrent(): Flow<List<WeatherDTO>> {
        return weatherDao.getCurrentForecast()
    }

    override suspend fun deleteAllCurrent() {
        weatherDao.deleteAllCurrent()
    }

    override suspend fun insertHourForecast(hourForecast: WeatherDTO) {
        weatherDao.insertHourForecast(hourForecast)
    }

    // here all functions that work on the cached Favorite weather data
    override suspend fun removeCityEntries(cityId: Int) {
        weatherDao.removeCityEntries(cityId)
    }

    override suspend fun addCityEntry(entry: FavoriteWeatherDTO) {
        weatherDao.addCityEntry(entry)
    }

    override fun getCityForecast(cityId: Int): Flow<List<FavoriteWeatherDTO>> {
        return weatherDao.getCityForecast(cityId)
    }

    override fun getAllCities(): Flow<List<FavoriteWeatherDTO>> {
        return weatherDao.getAllCities()
    }

    override suspend fun deleteAlert(alert: Alert) {
        weatherDao.deleteAlert(alert)
    }

    override suspend fun addAlert(alert: Alert) {
        weatherDao.addAlert(alert)
    }

    override fun getAlerts(): Flow<List<Alert>> {
        return weatherDao.getAlerts()
    }
}