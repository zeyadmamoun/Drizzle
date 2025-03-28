package com.example.drizzle.repository

import com.example.drizzle.data.LocalDataSource
import com.example.drizzle.model.current.WeatherDTO
import com.example.drizzle.network.RemoteDataSource
import com.example.drizzle.ui.theme.iconsMap

class WeatherRepositoryImpl(private val localDataSource: LocalDataSource) : WeatherRepository {

    override suspend fun getCurrentWeather(
        isOnline: Boolean,
        lat: Double,
        lon: Double
    ): WeatherDTO {
        return RemoteDataSource.getCurrentWeather(lat, lon).toWeatherObject()
    }

    override suspend fun getHourlyWeather(lat: Double, lon: Double): List<WeatherDTO> {
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
                icon = iconsMap[item.weather[0].icon]!!
            )
        }.toList()
    }
}