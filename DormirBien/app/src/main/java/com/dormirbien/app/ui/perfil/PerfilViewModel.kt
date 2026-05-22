package com.dormirbien.app.ui.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dormirbien.app.data.repository.Usuario
import com.dormirbien.app.data.repository.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface PerfilUiState {
    data object Loading : PerfilUiState
    data class Success(val usuario: Usuario?) : PerfilUiState
}

@HiltViewModel
class PerfilViewModel @Inject constructor(
    private val repo: UsuarioRepository,
) : ViewModel() {

    val uiState: StateFlow<PerfilUiState> = repo.observe()
        .map<Usuario?, PerfilUiState> { PerfilUiState.Success(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), PerfilUiState.Loading)

    fun save(nombre: String, edad: Int, peso: Float) {
        viewModelScope.launch {
            val existing = repo.get()
            repo.upsert(Usuario(id = existing?.id ?: 0L, nombre = nombre, edad = edad, peso = peso))
        }
    }
}
