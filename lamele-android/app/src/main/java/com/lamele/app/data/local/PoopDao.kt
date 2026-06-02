package com.lamele.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PoopDao {
    @Query("SELECT * FROM poop_records ORDER BY timeMillis DESC")
    fun observeAll(): Flow<List<PoopRecordEntity>>

    @Query("SELECT * FROM poop_records ORDER BY timeMillis DESC")
    suspend fun getAll(): List<PoopRecordEntity>

    @Query("SELECT * FROM poop_records WHERE id = :id")
    suspend fun getById(id: Long): PoopRecordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: PoopRecordEntity): Long

    @Update
    suspend fun update(entity: PoopRecordEntity)

    @Query("DELETE FROM poop_records WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM poop_records")
    suspend fun deleteAll()
}
