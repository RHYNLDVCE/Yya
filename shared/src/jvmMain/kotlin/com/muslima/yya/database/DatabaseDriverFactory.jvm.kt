package com.muslima.yya.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        val dbFile = java.io.File("quiz.db")
        val exists = dbFile.exists()
        val driver = JdbcSqliteDriver("jdbc:sqlite:quiz.db")
        if (!exists) {
            QuizDatabase.Schema.create(driver)
        }
        return driver
    }
}
