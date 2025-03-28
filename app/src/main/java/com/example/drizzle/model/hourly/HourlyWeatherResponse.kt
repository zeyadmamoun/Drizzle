package com.example.drizzle.model.hourly

import kotlinx.serialization.Serializable

@Serializable
data class HourlyWeatherResponse(
    val list: List<Item>,
    val city: City
)