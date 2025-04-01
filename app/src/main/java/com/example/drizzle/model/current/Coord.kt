package com.example.drizzle.model.current

import kotlinx.serialization.Serializable

@Serializable
data class Coord(
    val lat: Double = 0.0,
    val lon: Double = 0.0
)