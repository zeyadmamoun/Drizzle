package com.example.drizzle.model.alert

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "alerts")
data class Alert(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val locationName: String,
    val latitude: Double,
    val longitude: Double,
    val hour: Int,
    val minute: Int,
    var isActive: Boolean = true
)
