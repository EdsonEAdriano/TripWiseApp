package com.example.tripwiseapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.tripwiseapp.entity.Trip

@Dao
interface TripDAO {
    @Insert
    suspend fun insertTrip(trip: Trip)

    @Query("SELECT * FROM Trip order by id desc")
    suspend fun getAllTrips(): List<Trip>

    @Delete
    suspend fun deleteTrip(trip: Trip)
}