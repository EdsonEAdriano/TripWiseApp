package com.example.tripwiseapp.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Trip(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val destiny: String,
    val type: String,
    val startDate: String,
    val endDate: String,
    val budget: Double
)