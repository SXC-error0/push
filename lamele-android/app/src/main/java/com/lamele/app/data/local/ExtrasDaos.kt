package com.lamele.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ToiletDao {
    @Query("SELECT * FROM toilets ORDER BY id DESC")
    fun observeToilets(): Flow<List<ToiletEntity>>

    @Query("SELECT * FROM toilets WHERE id = :id")
    suspend fun getToilet(id: Long): ToiletEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(t: ToiletEntity): Long

    @Query("SELECT * FROM toilet_reviews WHERE toiletId = :toiletId ORDER BY createdAt DESC")
    fun observeReviews(toiletId: Long): Flow<List<ToiletReviewEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(r: ToiletReviewEntity): Long

    @Query("DELETE FROM toilets") suspend fun deleteAllToilets()
    @Query("DELETE FROM toilet_reviews") suspend fun deleteAllReviews()
}

@Dao
interface SocialDao {
    @Query("SELECT * FROM feed_posts ORDER BY createdAt DESC")
    fun observeFeed(): Flow<List<FeedPostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeed(f: FeedPostEntity): Long

    @Query("SELECT * FROM tree_holes ORDER BY createdAt DESC")
    fun observeTreeHoles(): Flow<List<TreeHoleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTreeHole(t: TreeHoleEntity): Long

    @Query("SELECT * FROM danmaku ORDER BY createdAt DESC LIMIT 120")
    fun observeDanmaku(): Flow<List<DanmakuEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDanmaku(d: DanmakuEntity): Long

    @Query("SELECT * FROM friends ORDER BY id ASC")
    fun observeFriends(): Flow<List<FriendEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFriend(f: FriendEntity): Long

    @Query("SELECT COUNT(*) FROM friends")
    suspend fun friendCount(): Int

    @Query("DELETE FROM feed_posts")
    suspend fun deleteAllFeeds()

    @Query("DELETE FROM tree_holes")
    suspend fun deleteAllHoles()

    @Query("DELETE FROM danmaku") suspend fun deleteAllDanmaku()

    @Query("DELETE FROM friends")
    suspend fun clearFriends()
}
