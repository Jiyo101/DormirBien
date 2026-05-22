package com.dormirbien.app.ui.ajustes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dormirbien.app.data.local.AlarmPreferences
import com.dormirbien.app.data.repository.Ajustes
import com.dormirbien.app.data.repository.AjustesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AjustesUiData(
    val modoOscuroActivado: Boolean = true,
    val tiempoMedioDormir:  Float   = 8f,
    val sonidoAlarma:       String  = "",
)

sealed interface AjustesUiState {
    data object Loading : AjustesUiState
    data class Success(val data: AjustesUiData) : AjustesUiState
}

@HiltViewModel
class AjustesViewModel @Inject constructor(
    private val ajustesRepo: AjustesRepository,
    private val prefs:       AlarmPreferences,
) : ViewModel() {

    val uiState: StateFlow<AjustesUiState> = combine(
        ajustesRepo.observe(),
        prefs.userSettings,
    ) { ajustes, settings ->
        AjustesUiState.Success(AjustesUiData(
            modoOscuroActivado = ajustes?.modoOscuroActivado ?: true,
            tiempoMedioDormir  = ajustes?.tiempoMedioDormir  ?: 8f,
            sonidoAlarma       = settings.soundUri,
        ))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AjustesUiState.Loading)

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            val current = ajustesRepo.get() ?: Ajustes()
            ajustesRepo.upsert(current.copy(modoOscuroActivado = enabled))
        }
    }
}
