package com.example.drizzle.model.hourly

import com.example.drizzle.model.current.Main
import com.example.drizzle.model.current.Weather
import com.example.drizzle.model.current.Wind
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val dt: Int,
    val dt_txt: String,
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind
)