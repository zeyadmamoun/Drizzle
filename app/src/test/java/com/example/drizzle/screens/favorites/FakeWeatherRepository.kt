package com.example.drizzle.screens.favorites

import com.example.drizzle.model.alert.Alert
import com.example.drizzle.model.current.FavoriteWeatherDTO
import com.example.drizzle.model.current.WeatherDTO
import com.example.drizzle.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeWeatherRepository(
    private val favoriteList: MutableList<WeatherDTO> = mutableListOf(),
    private val alerts: MutableList<Alert> = mutableListOf()
) : WeatherRepository {
    override suspend fun getCurrentWeather(
        isOnline: Boolean,
        lat: Double,
        lon: Double
    ): WeatherDTO {
        TODO("Not yet implemented")
    }

    override suspend fun getHourlyForecast(lat: Double, lon: Double): List<WeatherDTO> {
        TODO("Not yet implemented")
    }

    override fun getLocalCurrentWeather(): Flow<List<WeatherDTO>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteLocalCurrentWeather() {
        TODO("Not yet implemented")
    }

    override suspend fun insertLocalCurrentEntry(hourForecast: WeatherDTO) {
        TODO("Not yet implemented")
    }

    override suspend fun removeCityEntries(cityId: Int) {
        favoriteList.forEach {
            if (cityId == it.cityId)
                favoriteList.remove(it)
        }
    }

    override suspend fun addCityEntry(entry: FavoriteWeatherDTO) {
        favoriteList.add(entry.toWeatherObject())
    }

    override suspend fun getCityForecast(cityId: Int): Flow<List<WeatherDTO>> = flow{
        emit(favoriteList.filter { cityId == it.cityId })
    }

    override suspend fun getAllCities(): Flow<List<WeatherDTO>> = flow {
        emit(favoriteList)
    }

    override suspend fun addAlert(alert: Alert) {
        alerts.add(alert)
    }

    override suspend fun deleteAlert(alert: Alert) {
        alerts.remove(alert)
    }

    override fun getAlerts(): Flow<List<Alert>> = flow {
        emit(alerts)
    }
}