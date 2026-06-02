package com.lamele.app.data

import com.lamele.app.data.local.PoopDao
import com.lamele.app.data.local.PoopRecordEntity
import kotlinx.coroutines.flow.Flow

class PoopRepository(private val dao: PoopDao) {
    val records: Flow<List<PoopRecordEntity>> = dao.observeAll()

    suspend fun getById(id: Long): PoopRecordEntity? = dao.getById(id)

    suspend fun insert(entity: PoopRecordEntity): Long = dao.insert(entity)

    suspend fun update(entity: PoopRecordEntity) = dao.update(entity)

    suspend fun deleteAll() = dao.deleteAll()
}
