package com.example.dailyexpensetracker.core.constants

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS expense_table_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                title TEXT NOT NULL,
                amount REAL NOT NULL,
                date TEXT NOT NULL,
                type TEXT NOT NULL
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            INSERT INTO expense_table_new (id, title, amount, date, type)
            SELECT id, title, amount, date, type FROM expense_table
            """.trimIndent()
        )

        db.execSQL("DROP TABLE expense_table")
        db.execSQL("ALTER TABLE expense_table_new RENAME TO expense_table")
    }
}