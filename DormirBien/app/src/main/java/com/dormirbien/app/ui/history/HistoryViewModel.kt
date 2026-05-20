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
        val records:  List<SleepRecord>,
        val avg:      String,
        val goodDays: Int,
        val streak:   Int,
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

    val uiState: StateFlow<HistoryUiState> = repo.observeAll()
        .map { records -> buildState(records) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HistoryUiState.Loading)

    fun prevMonth() { if (_month.value == 0) { _month.value = 11; _year.value-- } else _month.value-- }
    fun nextMonth() { if (_month.value == 11) { _month.value = 0; _year.value++ } else _month.value++ }

    fun saveReview(record: SleepRecord, stars: Int, feeling: String) {
        viewModelScope.launch {
            repo.upsert(record.copy(stars = stars, feeling = feeling))
        }
    }

    private fun buildState(records: List<SleepRecord>): HistoryUiState.Success {
        // Group by day and compute daily totals
        val byDay      = records.groupBy { it.dateKey }
        val dailySums  = byDay.mapValues { (_, recs) -> recs.sumOf { it.hours.toDouble() }.toFloat() }
        val daysWithH  = dailySums.values.filter { it > 0f }

        val avg   = if (daysWithH.isEmpty()) "--" else "%.1fh".format(daysWithH.sum() / daysWithH.size)
        val good  = daysWithH.count { it >= 7f }

        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val cal = Calendar.getInstance()
        var streak = 0
        for ((dayKey, daySum) in dailySums.entries.sortedByDescending { it.key }) {
            if (dayKey == fmt.format(cal.time) && daySum >= 7f) {
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
            s        = state as HistoryUiState.Success,
            year     = year, month = month,
            onPrev   = vm::prevMonth, onNext = vm::nextMonth,
            onReview = { rec, stars, feeling -> vm.saveReview(rec, stars, feeling) },
        )
    }
}

// ── Screen ────────────────────────────────────────────────────────────────────
@Composable
private fun HistoryScreen(
    s:        HistoryUiState.Success,
    year:     Int, month: Int,
    onPrev:   () -> Unit, onNext: () -> Unit,
    onReview: (SleepRecord, Int, String) -> Unit,
) {
    var reviewRecord by remember { mutableStateOf<SleepRecord?>(null) }
    reviewRecord?.let { rec ->
        com.dormirbien.app.ui.components.ReviewDialog(
            onDismiss = { reviewRecord = null },
            onSave    = { stars, feeling -> onReview(rec, stars, feeling); reviewRecord = null },
        )
    }

    val sorted = s.records.sortedWith(
        compareByDescending<SleepRecord> { it.dateKey }.thenByDescending { it.id }
    )

    Column(
        Modifier.fillMaxSize().background(BG).verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Historial", color = TXT, fontSize = 22.sp, fontWeight = FontWeight.Bold)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatCard(s.avg,           "Media",  ACC,  Modifier.weight(1f))
            StatCard("${s.goodDays}", "Días ✓", GRN,  Modifier.weight(1f))
            StatCard("${s.streak}d",  "Racha",  ACC2, Modifier.weight(1f))
        }

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
                CalGrid(year, month, s.records) { rec -> reviewRecord = rec }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            LegendDot(GRN, "+7h ideal"); LegendDot(YEL, "5-7h"); LegendDot(RED, "-5h")
        }

        Text("TODAS LAS SESIONES", color = TXT3, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)

        if (sorted.isEmpty()) {
            Box(Modifier.fillMaxWidth().padding(vertical = 32.dp), Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🌙", fontSize = 40.sp)
                    Spacer(Modifier.height(12.dp))
                    Text("Aún no hay registros.\nPulsa \"Buenas noches\" esta noche\npara empezar tu historial.",
                        color = TXT3, fontSize = 13.sp, textAlign = TextAlign.Center, lineHeight = 20.sp)
                }
            }
        } else {
            sorted.forEach { rec ->
                SleepRow(rec) { reviewRecord = rec }
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun CalGrid(year: Int, month: Int, records: List<SleepRecord>, onDayClick: (SleepRecord) -> Unit) {
    val fmt   = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val cal   = Calendar.getInstance().apply { set(year, month, 1) }
    val days  = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    var dow   = cal.get(Calendar.DAY_OF_WEEK) - 2; if (dow < 0) dow = 6
    val today = Calendar.getInstance()
    val byDay = records.groupBy { it.dateKey }
    val cells = dow + days; val rows = (cells + 6) / 7
    repeat(rows) rows@{ row ->
        Row(Modifier.fillMaxWidth()) {
            repeat(7) { col ->
                val day = row * 7 + col - dow + 1
                if (day < 1 || day > days) { Box(Modifier.weight(1f).aspectRatio(1f)); return@repeat }
                val dayCal  = Calendar.getInstance().apply { set(year, month, day) }
                val key     = fmt.format(dayCal.time)
                val dayRecs = byDay[key] ?: emptyList()
                val sum     = dayRecs.sumOf { it.hours.toDouble() }.toFloat()
                val latest  = dayRecs.maxByOrNull { it.id }
                val isToday = dayCal.get(Calendar.YEAR)         == today.get(Calendar.YEAR) &&
                              dayCal.get(Calendar.MONTH)        == today.get(Calendar.MONTH) &&
                              dayCal.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)
                val color   = when { sum <= 0f -> null; sum < 5f -> RED; sum < 7f -> YEL; else -> GRN }
                Box(
                    Modifier.weight(1f).aspectRatio(1f).padding(2.dp)
                        .background(color?.copy(.13f) ?: CARD2, RoundedCornerShape(7.dp))
                        .border(if (isToday) 1.5.dp else 1.dp,
                            if (isToday) ACC else color?.copy(.28f) ?: TXT3.copy(.1f),
                            RoundedCornerShape(7.dp))
                        .then(if (latest != null) Modifier.clickable { onDayClick(latest) } else Modifier),
                    Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("$day", color = if (isToday) ACC else color ?: TXT3,
                            fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                        if (sum > 0f)
                            Text("%.1fh".format(sum), color = color!!.copy(.8f), fontSize = 7.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun SleepRow(rec: SleepRecord, onRate: () -> Unit) {
    val col  = when { rec.hours <= 0f -> TXT3; rec.hours < 5f -> RED; rec.hours < 7f -> YEL; else -> GRN }
    val lbl  = when { rec.hours <= 0f -> "Sin datos"; rec.hours < 5f -> "Insuficiente"; rec.hours < 7f -> "Correcto"; else -> "Ideal" }
    val disp = try {
        val from = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val to   = SimpleDateFormat("EEE d MMM", Locale("es", "ES"))
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
