package com.example.drizzle.repository

import com.example.drizzle.data.LocalDataSource
import com.example.drizzle.model.current.FavoriteWeatherDTO
import com.example.drizzle.model.current.WeatherDTO
import com.example.drizzle.network.RemoteDataSource
import com.example.drizzle.ui.theme.iconsMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WeatherRepositoryImpl(private val localDataSource: LocalDataSource) : WeatherRepository {

    // gets The current Weather conditions from the server
    override suspend fun getCurrentWeather(
        isOnline: Boolean,
        lat: Double,
        lon: Double
    ): WeatherDTO {
        return RemoteDataSource.getCurrentWeather(lat, lon).toWeatherObject()
    }

    // get the forecast for the next 5 days every three hours from server
    override suspend fun getHourlyForecast(lat: Double, lon: Double): List<WeatherDTO> {
        val result = RemoteDataSource.getHourlyWeather(lat,lon)
        return result.list.map { item ->
            WeatherDTO(
                weatherLat = result.city.coord.lat,
                weatherLon = result.city.coord.lon,
                weatherDesc = item.weather[0].description,
                name = result.city.name,
                country = result.city.country,
                windSpeed = item.wind.speed,
                humidity = item.main.humidity,
                mainTemp = item.main.temp.toInt(),
                date = item.dt,
                timezone = result.city.timezone,
                feelsLike = item.main.feels_like.toInt(),
                icon = iconsMap[item.weather[0].icon]!!,
                cityId = result.city.id
            )
        }.toList()
    }

    override fun getLocalCurrentWeather(): Flow<List<WeatherDTO>> {
        return localDataSource.getAllCurrent()
    }

    override suspend fun deleteLocalCurrentWeather() {
        localDataSource.deleteAllCurrent()
    }

    override suspend fun insertLocalCurrentEntry(hourForecast: WeatherDTO) {
        localDataSource.insertHourForecast(hourForecast)
    }

    override suspend fun removeCityEntries(cityId: Int) {
        localDataSource.removeCityEntries(cityId)
    }

    override suspend fun addCityEntry(entry: FavoriteWeatherDTO) {
        localDataSource.addCityEntry(entry)
    }

    override suspend fun getCityForecast(cityId: Int): Flow<List<WeatherDTO>> {
        val cityForecast = localDataSource.getCityForecast(cityId).map { item ->
            item.map { subItem -> subItem.toWeatherObject() }.toList()
        }
        return cityForecast
    }

    override suspend fun getAllCities(): Flow<List<WeatherDTO>> {
        val allCities = localDataSource.getAllCities().map { item ->
            item.map { subItem -> subItem.toWeatherObject() }.toList()
        }
        return allCities
    }
}