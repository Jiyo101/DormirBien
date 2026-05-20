package com.dormirbien.app.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(
    tableName = "sleep_records",
    indices = [Index("dateKey")],
)
data class SleepRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateKey:   String,
    val hours:     Float  = 0f,
    val stars:     Int    = 0,
    val feeling:   String = "",
    val createdAt: Long   = System.currentTimeMillis(),
)

@Dao
interface SleepDao {
    @Query("SELECT * FROM sleep_records ORDER BY dateKey DESC, id DESC")
    fun observeAll(): Flow<List<SleepRecordEntity>>

    @Query("SELECT * FROM sleep_records WHERE dateKey = :key ORDER BY id DESC LIMIT 1")
    suspend fun getByKey(key: String): SleepRecordEntity?

    @Query("SELECT * FROM sleep_records WHERE dateKey = :key AND stars = 0 AND feeling = '' ORDER BY id DESC LIMIT 1")
    suspend fun getLatestUnreviewedByKey(key: String): SleepRecordEntity?

    @Upsert
    suspend fun upsert(record: SleepRecordEntity)
}

@Database(entities = [SleepRecordEntity::class], version = 2, exportSchema = false)
abstract class SleepDatabase : RoomDatabase() {
    abstract fun sleepDao(): SleepDao
    companion object { const val NAME = "sleep_db" }
}
