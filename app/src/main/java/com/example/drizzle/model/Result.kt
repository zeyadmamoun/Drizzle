package com.example.drizzle.model

import com.example.drizzle.model.current.WeatherDTO

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Failed(val error: String) : Result<Nothing>()
    data object Loading : Result<Nothing>()
}