package com.dormirbien.app.data.repository

import com.dormirbien.app.data.local.SleepDao
import com.dormirbien.app.data.local.SleepRecordEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

data class SleepRecord(
    val id:        Long   = 0,
    val dateKey:   String,
    val hours:     Float  = 0f,
    val stars:     Int    = 0,
    val feeling:   String = "",
    val createdAt: Long   = System.currentTimeMillis(),
    val usuarioId: Long?  = null,
)

interface SleepRepository {
    fun observeAll(): Flow<List<SleepRecord>>
    suspend fun getByKey(key: String): SleepRecord?
    suspend fun getLatestUnreviewedByKey(key: String): SleepRecord?
    suspend fun upsert(record: SleepRecord)
}

@Singleton
class OfflineFirstSleepRepository @Inject constructor(
    private val dao: SleepDao,
) : SleepRepository {

    override fun observeAll(): Flow<List<SleepRecord>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun getByKey(key: String): SleepRecord? =
        dao.getByKey(key)?.toDomain()

    override suspend fun getLatestUnreviewedByKey(key: String): SleepRecord? =
        dao.getLatestUnreviewedByKey(key)?.toDomain()

    override suspend fun upsert(record: SleepRecord) =
        dao.upsert(record.toEntity())

    private fun SleepRecordEntity.toDomain() =
        SleepRecord(id, dateKey, hours, stars, feeling, createdAt, usuario_id)

    private fun SleepRecord.toEntity() =
        SleepRecordEntity(id, dateKey, hours, stars, feeling, createdAt, usuarioId)
}
