package com.example.drizzle.model.hourly

import com.example.drizzle.model.current.Coord
import kotlinx.serialization.Serializable

@Serializable
data class City(
    val id: Int,
    val name: String,
    val country: String,
    val coord: Coord,
    val timezone: Int
)
