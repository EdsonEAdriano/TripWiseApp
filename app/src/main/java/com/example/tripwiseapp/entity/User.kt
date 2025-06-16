package com.example.tripwiseapp.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,
    val name: String = "",
    val email: String = "",
    val password: String = ""
)