package com.example.tripwiseapp.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class Trip(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val destiny: String,
    val type: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val budget: Double
)