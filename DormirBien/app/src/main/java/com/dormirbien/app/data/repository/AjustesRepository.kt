package com.dormirbien.app.data.repository

import com.dormirbien.app.data.local.AjustesDao
import com.dormirbien.app.data.local.AjustesEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

data class Ajustes(
    val idAjuste:           Long    = 1L,
    val usuarioId:          Long?   = null,
    val tiempoMedioDormir:  Float   = 8f,
    val modoOscuroActivado: Boolean = true,
    val sonidoAlarma:       String  = "",
)

interface AjustesRepository {
    fun observe(): Flow<Ajustes?>
    suspend fun get(): Ajustes?
    suspend fun upsert(ajustes: Ajustes)
}

@Singleton
class OfflineFirstAjustesRepository @Inject constructor(
    private val dao: AjustesDao,
) : AjustesRepository {

    override fun observe(): Flow<Ajustes?> =
        dao.observe().map { it.firstOrNull()?.toDomain() }

    override suspend fun get(): Ajustes? =
        dao.get()?.toDomain()

    override suspend fun upsert(ajustes: Ajustes) =
        dao.upsert(ajustes.toEntity())

    private fun AjustesEntity.toDomain() =
        Ajustes(id_ajuste, usuario_id, tiempo_medio_dormir, modo_oscuro_activado, sonido_alarma)

    private fun Ajustes.toEntity() =
        AjustesEntity(idAjuste, usuarioId, tiempoMedioDormir, modoOscuroActivado, sonidoAlarma)
}
