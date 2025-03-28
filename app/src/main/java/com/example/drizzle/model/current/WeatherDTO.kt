package com.example.drizzle.model.current

data class WeatherDTO(
    val icon: Int,
    val weatherLat: Double,
    val weatherLon: Double,
    val weatherDesc: String,
    val mainTemp: Int,
    val humidity: Int,
    val windSpeed: Double,
    val date: Int,
    val country: String,
    val timezone: Int,
    val name: String,
    val feelsLike: Int
){
}
