package com.dormirbien.app.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "sleep_records")
data class SleepRecordEntity(
    @PrimaryKey val dateKey: String,   // "yyyy-MM-dd"
    val hours:   Float  = 0f,
    val stars:   Int    = 0,
    val feeling: String = "",
)

@Dao
interface SleepDao {
    @Query("SELECT * FROM sleep_records ORDER BY dateKey DESC")
    fun observeAll(): Flow<List<SleepRecordEntity>>

    @Query("SELECT * FROM sleep_records WHERE dateKey = :key LIMIT 1")
    suspend fun getByKey(key: String): SleepRecordEntity?

    @Upsert
    suspend fun upsert(record: SleepRecordEntity)
}

@Database(entities = [SleepRecordEntity::class], version = 1, exportSchema = false)
abstract class SleepDatabase : RoomDatabase() {
    abstract fun sleepDao(): SleepDao
    companion object { const val NAME = "sleep_db" }
}
