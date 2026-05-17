package com.dormirbien.app.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dormirbien.app.data.repository.SleepRecord
import com.dormirbien.app.data.repository.SleepRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

// ── Design tokens ─────────────────────────────────────────────────────────────
private val BG   = Color(0xFF070B14)
private val CARD = Color(0xFF0F1826)
private val CARD2= Color(0xFF162035)
private val ACC  = Color(0xFF5B7FFF)
private val ACC2 = Color(0xFF9D7BFF)
private val GRN  = Color(0xFF3ECF8E)
private val YEL  = Color(0xFFFFD166)
private val RED  = Color(0xFFFF6B6B)
private val TXT  = Color(0xFFDCE8FF)
private val TXT2 = Color(0xFF7A92B8)
private val TXT3 = Color(0xFF3A4F6E)

// ── UiState ───────────────────────────────────────────────────────────────────
sealed interface HistoryUiState {
    object Loading : HistoryUiState
    data class Success(
        val records:   List<SleepRecord>,
        val avg:       String,
        val goodDays:  Int,
        val streak:    Int,
    ) : HistoryUiState
}

// ── ViewModel ─────────────────────────────────────────────────────────────────
@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repo: SleepRepository,
) : ViewModel() {

    private val _year  = MutableStateFlow(Calendar.getInstance().get(Calendar.YEAR))
    private val _month = MutableStateFlow(Calendar.getInstance().get(Calendar.MONTH))

    val year:  StateFlow<Int> = _year
    val month: StateFlow<Int> = _month

    // Reactive — recomposes whenever Room emits new data
    val uiState: StateFlow<HistoryUiState> = repo.observeAll()
        .map { records -> buildState(records) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HistoryUiState.Loading)

    fun prevMonth() { if (_month.value == 0) { _month.value = 11; _year.value-- } else _month.value-- }
    fun nextMonth() { if (_month.value == 11) { _month.value = 0; _year.value++ } else _month.value++ }

    fun saveReview(dateKey: String, stars: Int, feeling: String) {
        viewModelScope.launch {
            val existing = repo.getByKey(dateKey)
            repo.upsert(SleepRecord(dateKey, existing?.hours ?: 0f, stars, feeling))
        }
    }

    private fun buildState(records: List<SleepRecord>): HistoryUiState.Success {
        val withH = records.filter { it.hours > 0f }
        val avg   = if (withH.isEmpty()) "--" else "%.1fh".format(withH.sumOf { it.hours.toDouble() } / withH.size)
        val good  = withH.count { it.hours >= 7f }
        val sorted = records.sortedByDescending { it.dateKey }
        var streak = 0
        val fmt    = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val cal    = Calendar.getInstance()
        for (rec in sorted) {
            if (rec.dateKey == fmt.format(cal.time) && rec.hours >= 7f) {
                streak++; cal.add(Calendar.DAY_OF_YEAR, -1)
            } else break
        }
        return HistoryUiState.Success(records, avg, good, streak)
    }
}

// ── Route ─────────────────────────────────────────────────────────────────────
@Composable
fun HistoryRoute(vm: HistoryViewModel = hiltViewModel()) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    val year  by vm.year.collectAsStateWithLifecycle()
    val month by vm.month.collectAsStateWithLifecycle()

    when (state) {
        HistoryUiState.Loading -> Box(Modifier.fillMaxSize().background(BG), Alignment.Center) {
            CircularProgressIndicator(color = ACC)
        }
        is HistoryUiState.Success -> HistoryScreen(
            s         = state as HistoryUiState.Success,
            year      = year, month = month,
            onPrev    = vm::prevMonth, onNext = vm::nextMonth,
            onReview  = { key, stars, feeling -> vm.saveReview(key, stars, feeling) },
        )
    }
}

// ── Screen ────────────────────────────────────────────────────────────────────
@Composable
private fun HistoryScreen(
    s:        HistoryUiState.Success,
    year:     Int, month: Int,
    onPrev:   () -> Unit, onNext: () -> Unit,
    onReview: (String, Int, String) -> Unit,
) {
    var reviewKey by remember { mutableStateOf<String?>(null) }
    reviewKey?.let { key ->
        com.dormirbien.app.ui.components.ReviewDialog(
            onDismiss = { reviewKey = null },
            onSave    = { stars, feeling -> onReview(key, stars, feeling); reviewKey = null },
        )
    }

    Column(
        Modifier.fillMaxSize().background(BG).verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Historial", color = TXT, fontSize = 22.sp, fontWeight = FontWeight.Bold)

        // Stats — all computed from real Room data
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatCard(s.avg,                 "Media",   ACC,  Modifier.weight(1f))
            StatCard("${s.goodDays}",       "Días ✓",  GRN,  Modifier.weight(1f))
            StatCard("${s.streak}d",        "Racha",   ACC2, Modifier.weight(1f))
        }

        // Calendar — real data only
        Card(colors = CardDefaults.cardColors(containerColor = CARD),
            shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(14.dp)) {
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                    IconButton(onPrev) { Text("◀", color = TXT2, fontSize = 16.sp) }
                    Text(monthName(month, year), color = TXT, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    IconButton(onNext) { Text("▶", color = TXT2, fontSize = 16.sp) }
                }
                Spacer(Modifier.height(6.dp))
                Row(Modifier.fillMaxWidth()) {
                    listOf("Lu","Ma","Mi","Ju","Vi","Sá","Do").forEach {
                        Text(it, color = TXT3, fontSize = 9.sp, fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                    }
                }
                Spacer(Modifier.height(4.dp))
                CalGrid(year, month, s.records) { key -> reviewKey = key }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            LegendDot(GRN, "+7h ideal"); LegendDot(YEL, "5-7h"); LegendDot(RED, "-5h")
        }

        Text("ÚLTIMAS NOCHES", color = TXT3, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)

        if (s.records.isEmpty()) {
            Box(Modifier.fillMaxWidth().padding(vertical = 32.dp), Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🌙", fontSize = 40.sp)
                    Spacer(Modifier.height(12.dp))
                    Text("Aún no hay registros.\nPulsa \"Buenas noches\" esta noche\npara empezar tu historial.",
                        color = TXT3, fontSize = 13.sp, textAlign = TextAlign.Center, lineHeight = 20.sp)
                }
            }
        } else {
            s.records.sortedByDescending { it.dateKey }.take(14).forEach { rec ->
                SleepRow(rec) { reviewKey = rec.dateKey }
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun CalGrid(year: Int, month: Int, records: List<SleepRecord>, onDayClick: (String) -> Unit) {
    val fmt    = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val cal    = Calendar.getInstance().apply { set(year, month, 1) }
    val days   = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    var dow    = cal.get(Calendar.DAY_OF_WEEK) - 2; if (dow < 0) dow = 6
    val today  = Calendar.getInstance()
    val recMap = records.associateBy { it.dateKey }
    val cells  = dow + days; val rows = (cells + 6) / 7
    repeat(rows) rows@{ row ->
        Row(Modifier.fillMaxWidth()) {
            repeat(7) { col ->
                val day = row * 7 + col - dow + 1
                if (day < 1 || day > days) { Box(Modifier.weight(1f).aspectRatio(1f)); return@repeat }
                val dayCal = Calendar.getInstance().apply { set(year, month, day) }
                val key    = fmt.format(dayCal.time)
                val rec    = recMap[key]
                val isToday = dayCal.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                              dayCal.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                              dayCal.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)
                val col2   = when { rec == null || rec.hours <= 0f -> null; rec.hours < 5f -> RED; rec.hours < 7f -> YEL; else -> GRN }
                Box(
                    Modifier.weight(1f).aspectRatio(1f).padding(2.dp)
                        .background(col2?.copy(.13f) ?: CARD2, RoundedCornerShape(7.dp))
                        .border(if (isToday) 1.5.dp else 1.dp,
                            if (isToday) ACC else col2?.copy(.28f) ?: TXT3.copy(.1f),
                            RoundedCornerShape(7.dp))
                        .then(if (rec != null) Modifier.clickable { onDayClick(key) } else Modifier),
                    Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("$day", color = if (isToday) ACC else col2 ?: TXT3,
                            fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                        if (rec != null && rec.hours > 0f)
                            Text("%.1fh".format(rec.hours), color = col2!!.copy(.8f), fontSize = 7.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun SleepRow(rec: SleepRecord, onRate: () -> Unit) {
    val col  = when { rec.hours < 5f -> RED; rec.hours < 7f -> YEL; else -> GRN }
    val lbl  = when { rec.hours < 5f -> "Insuficiente"; rec.hours < 7f -> "Correcto"; else -> "Ideal" }
    val disp = try {
        val from = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val to   = SimpleDateFormat("EEE d MMM", Locale("es","ES"))
        to.format(from.parse(rec.dateKey)!!)
    } catch (e: Exception) { rec.dateKey }
    Surface(color = CARD, shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, TXT3.copy(.12f)), modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(11.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(9.dp).background(col, RoundedCornerShape(50)))
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(disp, color = TXT, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(lbl, color = TXT2, fontSize = 10.sp)
                    if (rec.stars > 0) Text("⭐".repeat(rec.stars), fontSize = 10.sp)
                    if (rec.feeling.isNotEmpty()) Text(rec.feeling.split(" ").first(), fontSize = 10.sp)
                }
            }
            if (rec.hours > 0f) Text("%.1fh".format(rec.hours), color = col, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.width(8.dp))
            OutlinedButton(onRate, contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                modifier = Modifier.height(28.dp)) {
                Text("Valorar", color = TXT2, fontSize = 10.sp)
            }
        }
    }
}

@Composable private fun StatCard(v: String, label: String, c: Color, mod: Modifier) {
    Card(colors = CardDefaults.cardColors(containerColor = CARD), shape = RoundedCornerShape(12.dp), modifier = mod) {
        Column(Modifier.padding(11.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(v, color = c, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Text(label, color = TXT3, fontSize = 9.sp, letterSpacing = 1.sp)
        }
    }
}

@Composable private fun LegendDot(c: Color, l: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(8.dp).background(c, RoundedCornerShape(50))); Spacer(Modifier.width(4.dp))
        Text(l, color = TXT2, fontSize = 11.sp)
    }
}

private fun monthName(m: Int, y: Int) = listOf("Enero","Febrero","Marzo","Abril","Mayo","Junio",
    "Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre")[m] + " $y"
