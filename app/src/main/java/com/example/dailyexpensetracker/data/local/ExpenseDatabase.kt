package com.example.dailyexpensetracker.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.dailyexpensetracker.core.constants.MIGRATION_1_2
import com.example.dailyexpensetracker.core.utils.UriTypeConverter
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

// Defines a Room database with two entities: ExpenseEntity and UserEntity
@Database(
    entities = [ExpenseEntity::class, UserEntity::class],
    version = 2,                 // Database version for handling migrations
    exportSchema = false         // Prevents Room from exporting the schema to a folder
)
@TypeConverters(UriTypeConverter::class)
@Singleton
abstract class ExpenseDatabase : RoomDatabase() {

    // Abstract functions to access DAO interfaces
    abstract fun expenseDao(): ExpenseDao
    abstract fun userDao(): UserDao

    companion object {
        private const val DATABASE_NAME = "expense_database"

        // Volatile ensures visibility across threads
        @Volatile
        private var INSTANCE: ExpenseDatabase? = null

        // Singleton pattern to ensure only one instance of the database exists
        fun getInstance(@ApplicationContext context: Context): ExpenseDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        // Builds the database instance using Room
        private fun buildDatabase(context: Context): ExpenseDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ExpenseDatabase::class.java,
                DATABASE_NAME
            )
                .addMigrations(MIGRATION_1_2) // Adds a migration from version 1 to 2
                .fallbackToDestructiveMigration(false) // Prevents automatic destructive migration (data loss)
                .fallbackToDestructiveMigrationOnDowngrade(false) // Prevents destruction on downgrade
                .build()
        }
    }
}
