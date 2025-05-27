package com.example.tripwiseapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tripwiseapp.dao.TripDAO
import com.example.tripwiseapp.dao.UserDAO
import com.example.tripwiseapp.entity.Trip
import com.example.tripwiseapp.entity.User
import com.example.tripwiseapp.helpers.Converters

@Database(
    entities = [User::class, Trip::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao() : UserDAO
    abstract fun tripDao() : TripDAO

    companion object {
        @Volatile

        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "trips_database")
                    .addMigrations(MIGRATION_1_2)
                    .build()
                    .also { Instance = it }
            }
        }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {

                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `User` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                        `name` TEXT NOT NULL, 
                        `email` TEXT NOT NULL, 
                        `password` TEXT NOT NULL
                    )
                """.trimIndent())

                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `Trip` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                        `destiny` TEXT NOT NULL, 
                        `type` TEXT NOT NULL, 
                        `startDate` TEXT NOT NULL, 
                        `endDate` TEXT NOT NULL, 
                        `budget` REAL NOT NULL
                    )
                """.trimIndent())
            }
        }
    }
}

