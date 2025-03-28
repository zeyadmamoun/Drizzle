package com.example.drizzle.model.current

import kotlinx.serialization.Serializable

@Serializable
data class Sys(
    val country: String = "",
    val id: Int? = null,
    val sunrise:Int = 0,
    val sunset: Int = 0,
    val type: Int? = null,
    val pod: String = ""
)