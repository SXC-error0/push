package com.lamele.app.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Suppress("SpellCheckingInspection")
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `toilets` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `name` TEXT NOT NULL,
                `alias` TEXT,
                `city` TEXT,
                `latitude` REAL,
                `longitude` REAL,
                `createdAt` INTEGER NOT NULL
            )
            """.trimIndent(),
        )
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `toilet_reviews` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `toiletId` INTEGER NOT NULL,
                `cleanliness` INTEGER NOT NULL,
                `privacy` INTEGER NOT NULL,
                `paper` INTEGER NOT NULL,
                `flushPower` INTEGER NOT NULL,
                `smellAttack` INTEGER NOT NULL,
                `comfort` INTEGER NOT NULL,
                `paidPoopSuitability` INTEGER NOT NULL,
                `signal` INTEGER NOT NULL,
                `soulEcho` INTEGER NOT NULL,
                `comment` TEXT,
                `createdAt` INTEGER NOT NULL
            )
            """.trimIndent(),
        )
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `feed_posts` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `content` TEXT NOT NULL,
                `tags` TEXT,
                `isAnonymous` INTEGER NOT NULL,
                `linkedRecordId` INTEGER,
                `likeCount` INTEGER NOT NULL,
                `createdAt` INTEGER NOT NULL
            )
            """.trimIndent(),
        )
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `tree_holes` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `content` TEXT NOT NULL,
                `moodTag` TEXT,
                `likeCount` INTEGER NOT NULL,
                `createdAt` INTEGER NOT NULL
            )
            """.trimIndent(),
        )
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `danmaku` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `text` TEXT NOT NULL,
                `createdAt` INTEGER NOT NULL
            )
            """.trimIndent(),
        )
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `friends` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `nickname` TEXT NOT NULL,
                `mockWeeklyCount` INTEGER NOT NULL,
                `mockPaidMinutes` INTEGER NOT NULL
            )
            """.trimIndent(),
        )
    }
}
