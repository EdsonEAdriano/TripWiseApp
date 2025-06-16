package com.example.tripwiseapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.example.tripwiseapp.entity.Suggestion
import com.example.tripwiseapp.entity.Trip

@Dao
interface SuggestionDAO {
    @Insert
    suspend fun insertSuggestion(suggestion: Suggestion)

    @Query("SELECT * FROM Suggestion WHERE tripId =:id")
    suspend fun findByTripId(id : Int?) : Suggestion?
}