package com.example.tripwiseapp.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Trip(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Long,
    val destiny: String,
    val type: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val budget: Double
)