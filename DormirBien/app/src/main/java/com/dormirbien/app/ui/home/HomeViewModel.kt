package com.dormirbien.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dormirbien.app.data.local.AlarmPreferences
import com.dormirbien.app.data.local.AlarmState
import com.dormirbien.app.data.local.UserSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject
import kotlin.math.abs

// ── UiState ───────────────────────────────────────────────────────────────────

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(
        val wakeH:      Int,
        val wakeM:      Int,
        val onset:      Int,
        val alarm:      AlarmState,
        val options:    List<AlarmOption>,
        val selected:   AlarmOption?,
        val showModal:  Boolean,
    ) : HomeUiState
}

data class AlarmOption(
    val cycles:   Int,
    val wakeH:    Int,
    val wakeM:    Int,
    val totalMin: Int,
    val diffMin:  Int,
    val isBest:   Boolean,
)

// ── Actions ────────────────────────────────────────────────────────────────────

sealed interface HomeAction {
    data class HourDelta(val d: Int)   : HomeAction
    data class MinuteDelta(val d: Int) : HomeAction
    data class SetOnset(val v: Int)    : HomeAction
    data class Pick(val o: AlarmOption): HomeAction
    object OpenModal  : HomeAction
    object CloseModal : HomeAction
}

// ── ViewModel ─────────────────────────────────────────────────────────────────

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val prefs: AlarmPreferences,
) : ViewModel() {

    private val _wH         = MutableStateFlow(7)
    private val _wM         = MutableStateFlow(0)
    private val _onset      = MutableStateFlow(14)
    private val _showModal  = MutableStateFlow(false)
    private val _selected   = MutableStateFlow<AlarmOption?>(null)

    val uiState: StateFlow<HomeUiState> = combine(
        prefs.alarmState, _wH, _wM, _onset, _showModal, _selected,
    ) { arr ->
        @Suppress("UNCHECKED_CAST")
        val alarm  = arr[0] as AlarmState
        val wH     = arr[1] as Int; val wM = arr[2] as Int; val onset = arr[3] as Int
        val modal  = arr[4] as Boolean; val sel = arr[5] as AlarmOption?
        val now    = Calendar.getInstance()
        val opts   = computeOptions(now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE), onset, wH * 60 + wM)
        HomeUiState.Success(
            wakeH = wH, wakeM = wM, onset = onset, alarm = alarm,
            options = opts, selected = sel ?: opts.firstOrNull { it.isBest },
            showModal = modal,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState.Loading)

    init {
        viewModelScope.launch {
            prefs.userSettings.first().also {
                _wH.value = it.wakeHour; _wM.value = it.wakeMinute; _onset.value = it.onsetMinutes
            }
        }
    }

    fun onAction(a: HomeAction) = when (a) {
        is HomeAction.HourDelta    -> { _wH.update { (it + a.d + 24) % 24 }; save() }
        is HomeAction.MinuteDelta  -> { _wM.update { (it + a.d + 60) % 60 }; save() }
        is HomeAction.SetOnset     -> { _onset.value = a.v; save() }
        is HomeAction.Pick         -> _selected.value = a.o
        HomeAction.OpenModal       -> _showModal.value = true
        HomeAction.CloseModal      -> { _showModal.value = false; _selected.value = null }
    }

    private fun save() { viewModelScope.launch { prefs.saveSettings(_wH.value, _wM.value, _onset.value) } }

    fun fmtH(min: Int) = if (min % 60 == 0) "${min/60}h" else "${min/60}h ${min%60}m"

    private fun computeOptions(bedMin: Int, onset: Int, wakeTarget: Int): List<AlarmOption> {
        val start  = bedMin + onset
        var target = wakeTarget; if (target <= start) target += 1440
        val all = (1..9).map { c ->
            val wm = (start + c * 90) % 1440
            AlarmOption(c, wm / 60 % 24, wm % 60, c * 90, start + c * 90 - target, false)
        }
        val before = all.filter { it.diffMin <= 0 }.maxByOrNull { it.diffMin }
        val after  = all.filter { it.diffMin >  0 }.minByOrNull { it.diffMin }
        val best   = when {
            before != null && after != null ->
                if (abs(before.diffMin) <= abs(after.diffMin)) before else after
            else -> before ?: after
        }
        return listOfNotNull(best?.copy(isBest = true), if (best == before) after else before).filterNotNull()
    }
}
