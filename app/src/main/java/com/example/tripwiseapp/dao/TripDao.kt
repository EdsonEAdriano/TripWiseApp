package com.example.tripwiseapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.example.tripwiseapp.entity.Trip

@Dao
interface TripDAO {
    @Insert
    suspend fun insertTrip(trip: Trip)

    @Upsert
    suspend fun upsert(trip: Trip)

    @Query("SELECT * FROM Trip WHERE id =:id")
    suspend fun findById(id : Int) : Trip?

    @Query("SELECT * FROM Trip WHERE userId = :userId ORDER BY id DESC")
    suspend fun getAllTripsByUser(userId: Long): List<Trip>

    @Query("DELETE FROM Trip WHERE id = :id")
    suspend fun deleteTrip(id: Int)
}