package com.example.drizzle.model.current

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current")
data class WeatherDTO(
    @PrimaryKey(autoGenerate = true) val entryId: Int = 0,
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
)
