package com.dormirbien.app.data.repository

import com.dormirbien.app.data.local.UsuarioDao
import com.dormirbien.app.data.local.UsuarioEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

data class Usuario(
    val id:     Long   = 0,
    val nombre: String = "",
    val edad:   Int    = 0,
    val peso:   Float  = 0f,
)

interface UsuarioRepository {
    fun observe(): Flow<Usuario?>
    suspend fun get(): Usuario?
    suspend fun upsert(usuario: Usuario)
}

@Singleton
class OfflineFirstUsuarioRepository @Inject constructor(
    private val dao: UsuarioDao,
) : UsuarioRepository {

    override fun observe(): Flow<Usuario?> =
        dao.observe().map { it.firstOrNull()?.toDomain() }

    override suspend fun get(): Usuario? =
        dao.get()?.toDomain()

    override suspend fun upsert(usuario: Usuario) =
        dao.upsert(usuario.toEntity())

    private fun UsuarioEntity.toDomain() = Usuario(id, nombre, edad, peso)
    private fun Usuario.toEntity()       = UsuarioEntity(id, nombre, edad, peso)
}
