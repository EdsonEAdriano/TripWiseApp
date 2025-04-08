package com.example.tripwiseapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tripwiseapp.dao.TripDAO
import com.example.tripwiseapp.dao.UserDAO
import com.example.tripwiseapp.entity.Trip
import com.example.tripwiseapp.entity.User

@Database(
    entities = [User::class, Trip::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao() : UserDAO
    abstract fun tripDao() : TripDAO

    companion object {
        @Volatile

        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "trip_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}