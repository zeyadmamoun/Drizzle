package com.example.drizzle.model.current

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Main(
    @SerialName("feels_like")val feels_like: Double,
    @SerialName("humidity")val humidity: Int,
    @SerialName("pressure")val pressure: Int,
    @SerialName("temp")val temp: Double,
)