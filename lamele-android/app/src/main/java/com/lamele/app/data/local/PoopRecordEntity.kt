package com.lamele.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "poop_records")
data class PoopRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timeMillis: Long,
    val durationMinutes: Int,
    val city: String?,
    val sceneType: String,
    val amountLevel: String,
    val shapeType: String,
    val colorType: String?,
    val smoothLevel: String,
    val mood: String,
    val isPaidPoop: Boolean,
    val isPublic: Boolean,
    val note: String?,
    val aiComment: String?,
)
