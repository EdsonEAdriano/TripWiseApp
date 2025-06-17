package com.example.tripwiseapp.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Trip::class,
        parentColumns = ["id"],
        childColumns = ["tripId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Suggestion (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    var tripId: Int = 0,
    val suggestion: String = "",

    val createAt: String = getTodayDate()
)

fun getTodayDate(): String {
    val formatter = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
    return formatter.format(java.util.Date())
}