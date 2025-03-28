package com.example.drizzle.model.current

import kotlinx.serialization.Serializable

@Serializable
data class Weather(
    val icon: String,
    val description: String,
//    val icon: String,
//    val id: Int,
//    val main: String
)