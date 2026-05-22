package com.dormirbien.app.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// ── Entities ──────────────────────────────────────────────────────────────────

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String = "",
    val edad:   Int    = 0,
    val peso:   Float  = 0f,
)

@Entity(
    tableName = "ajustes",
    foreignKeys = [
        ForeignKey(
            entity        = UsuarioEntity::class,
            parentColumns = ["id"],
            childColumns  = ["usuario_id"],
            onDelete      = ForeignKey.SET_NULL,
        )
    ],
    indices = [Index("usuario_id")],
)
data class AjustesEntity(
    @PrimaryKey val id_ajuste:            Long    = 1L,
    val usuario_id:           Long?   = null,
    val tiempo_medio_dormir:  Float   = 8f,
    val modo_oscuro_activado: Boolean = true,
    val sonido_alarma:        String  = "",
)

@Entity(
    tableName = "sleep_records",
    foreignKeys = [
        ForeignKey(
            entity        = UsuarioEntity::class,
            parentColumns = ["id"],
            childColumns  = ["usuario_id"],
            onDelete      = ForeignKey.SET_NULL,
        )
    ],
    indices = [Index("dateKey"), Index("usuario_id")],
)
data class SleepRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateKey:    String,
    val hours:      Float  = 0f,
    val stars:      Int    = 0,
    val feeling:    String = "",
    val createdAt:  Long   = System.currentTimeMillis(),
    val usuario_id: Long?  = null,
)

// ── DAOs ──────────────────────────────────────────────────────────────────────

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

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios LIMIT 1")
    fun observe(): Flow<List<UsuarioEntity>>

    @Query("SELECT * FROM usuarios LIMIT 1")
    suspend fun get(): UsuarioEntity?

    @Upsert
    suspend fun upsert(usuario: UsuarioEntity)
}

@Dao
interface AjustesDao {
    @Query("SELECT * FROM ajustes WHERE id_ajuste = 1 LIMIT 1")
    fun observe(): Flow<List<AjustesEntity>>

    @Query("SELECT * FROM ajustes WHERE id_ajuste = 1 LIMIT 1")
    suspend fun get(): AjustesEntity?

    @Upsert
    suspend fun upsert(ajustes: AjustesEntity)
}

// ── Database ──────────────────────────────────────────────────────────────────

@Database(
    entities     = [UsuarioEntity::class, AjustesEntity::class, SleepRecordEntity::class],
    version      = 3,
    exportSchema = false,
)
abstract class SleepDatabase : RoomDatabase() {
    abstract fun sleepDao():   SleepDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun ajustesDao(): AjustesDao
    companion object { const val NAME = "sleep_db" }
}
