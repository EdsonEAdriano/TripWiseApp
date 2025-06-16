package com.example.tripwiseapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tripwiseapp.dao.SuggestionDAO
import com.example.tripwiseapp.dao.TripDAO
import com.example.tripwiseapp.dao.UserDAO
import com.example.tripwiseapp.entity.Suggestion
import com.example.tripwiseapp.entity.Trip
import com.example.tripwiseapp.entity.User
import com.example.tripwiseapp.helpers.Converters

@Database(
    entities = [User::class, Trip::class, Suggestion::class],
    version = 5
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao() : UserDAO
    abstract fun tripDao() : TripDAO
    abstract fun suggestionDao() : SuggestionDAO

    companion object {
        @Volatile

        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "trips_database")
                    .fallbackToDestructiveMigration()
                    .addMigrations(MIGRATION_1_2)
                    .addMigrations(MIGRATION_3_4)
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
                        `userId` INTEGER NOT NULL,
                        `destiny` TEXT NOT NULL, 
                        `type` TEXT NOT NULL, 
                        `startDate` TEXT NOT NULL, 
                        `endDate` TEXT NOT NULL, 
                        `budget` REAL NOT NULL,
                        FOREIGN KEY(userId) REFERENCES User(id) ON DELETE CASCADE
                    )
                """.trimIndent())
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `Suggestion` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `tripId` INTEGER NOT NULL,
                        `suggestion` TEXT NOT NULL,
                        `createAt` TEXT NOT NULL DEFAULT (date('now')),
                        FOREIGN KEY(tripId) REFERENCES Trip(id) ON DELETE CASCADE
                    )
                """.trimIndent())
            }
        }
    }
}

