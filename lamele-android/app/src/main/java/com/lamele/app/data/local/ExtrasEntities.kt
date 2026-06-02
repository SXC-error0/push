package com.lamele.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "toilets")
data class ToiletEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val alias: String?,
    val city: String?,
    val latitude: Double?,
    val longitude: Double?,
    val createdAt: Long = System.currentTimeMillis(),
)

@Entity(tableName = "toilet_reviews")
data class ToiletReviewEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val toiletId: Long,
    val cleanliness: Int,
    val privacy: Int,
    val paper: Int,
    val flushPower: Int,
    val smellAttack: Int,
    val comfort: Int,
    val paidPoopSuitability: Int,
    val signal: Int,
    val soulEcho: Int,
    val comment: String?,
    val createdAt: Long = System.currentTimeMillis(),
)

@Entity(tableName = "feed_posts")
data class FeedPostEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val content: String,
    val tags: String?,
    val isAnonymous: Boolean,
    val linkedRecordId: Long?,
    val likeCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
)

@Entity(tableName = "tree_holes")
data class TreeHoleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val content: String,
    val moodTag: String?,
    val likeCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
)

@Entity(tableName = "danmaku")
data class DanmakuEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val text: String,
    val createdAt: Long = System.currentTimeMillis(),
)

@Entity(tableName = "friends")
data class FriendEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nickname: String,
    val mockWeeklyCount: Int,
    val mockPaidMinutes: Int,
)
