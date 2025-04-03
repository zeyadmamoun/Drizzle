package com.example.drizzle.model.current

import com.example.drizzle.ui.theme.iconsMap
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherResponse(
    val base: String,
    val cod: Int,
    val coord: Coord,
    val dt: Int,
    val id: Int,
    val main: Main,
    val name: String,
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
){
    fun toWeatherObject() = WeatherDTO(
        weatherLat = coord.lat,
        weatherLon = coord.lon,
        weatherDesc = weather[0].description,
        mainTemp = main.temp.toInt(),
        humidity = main.humidity,
        windSpeed = wind.speed,
        date = dt,
        country = sys.country,
        timezone = timezone,
        name = name,
        feelsLike = main.feels_like.toInt(),
        icon = iconsMap[weather[0].icon]!!,
        cityId = id
    )
}