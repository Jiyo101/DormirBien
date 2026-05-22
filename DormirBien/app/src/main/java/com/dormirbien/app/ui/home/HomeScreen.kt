package com.dormirbien.app.ui.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dormirbien.app.alarm.AlarmScheduler
import com.dormirbien.app.data.local.AlarmState
import com.dormirbien.app.ui.theme.LocalAppColors
import kotlinx.coroutines.delay
import kotlin.math.*

// ── Route ─────────────────────────────────────────────────────────────────────

@Composable
fun HomeRoute(
    onScheduleAlarms: (Int, Int, Int, String) -> Unit,
    onCancelAlarms:   () -> Unit,
    onCancelBackup:   () -> Unit,
    onShowMiuiGuide:  () -> Unit = {},
    vm:               HomeViewModel = hiltViewModel(),
) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    val c = LocalAppColors.current
    if (state is HomeUiState.Loading) {
        Box(Modifier.fillMaxSize().background(c.bg), Alignment.Center) {
            CircularProgressIndicator(color = c.acc)
        }
        return
    }
    val s = state as HomeUiState.Success

    if (s.showModal) {
        AlarmModal(
            options   = s.options,
            selected  = s.selected,
            onPick    = { vm.onAction(HomeAction.Pick(it)) },
            onConfirm = {
                val opt = s.selected ?: return@AlarmModal
                onScheduleAlarms(opt.wakeH, opt.wakeM, opt.cycles, vm.fmtH(opt.totalMin))
                vm.onAction(HomeAction.CloseModal)
            },
            onDismiss = { vm.onAction(HomeAction.CloseModal) },
            fmt       = vm::fmtH,
        )
    }

    HomeScreen(
        s               = s,
        onAction        = vm::onAction,
        onCancelAlarms  = onCancelAlarms,
        onCancelBackup  = onCancelBackup,
        onOpenModal     = { vm.onAction(HomeAction.OpenModal) },
        onShowMiuiGuide = onShowMiuiGuide,
    )
}

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
private fun HomeScreen(
    s:               HomeUiState.Success,
    onAction:        (HomeAction) -> Unit,
    onCancelAlarms:  () -> Unit,
    onCancelBackup:  () -> Unit,
    onOpenModal:     () -> Unit,
    onShowMiuiGuide: () -> Unit,
) {
    val c = LocalAppColors.current
    Column(
        Modifier.fillMaxSize().background(c.bg).padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Spacer(Modifier.height(12.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            LogoRow()
            Spacer(Modifier.height(6.dp))
            LiveClock()
        }

        WakePicker(
            h      = s.wakeH,
            m      = s.wakeM,
            onHour = { onAction(HomeAction.HourDelta(it)) },
            onMin  = { onAction(HomeAction.MinuteDelta(it)) },
        )

        OnsetCard(
            onset   = s.onset,
            onOnset = { onAction(HomeAction.SetOnset(it)) },
        )

        AlarmStatusCard(s.alarm, onCancelAlarms, onCancelBackup)

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            MoonButton(onOpenModal)
            Spacer(Modifier.height(4.dp))
            MiuiGuideCard(onShowGuide = onShowMiuiGuide)
        }

        Spacer(Modifier.height(8.dp))
    }
}

// ── Logo ──────────────────────────────────────────────────────────────────────

@Composable
private fun LogoRow() {
    val c = LocalAppColors.current
    Row(
        modifier              = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment     = Alignment.CenterVertically,
    ) {
        Canvas(Modifier.size(36.dp)) { drawLogo(size.minDimension) }
        Spacer(Modifier.width(10.dp))
        Text("Dormir", color = c.txt,  fontSize = 20.sp, fontWeight = FontWeight.Light,    letterSpacing = 2.sp)
        Text("Bien",   color = c.acc,  fontSize = 20.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 2.sp)
    }
}

private fun DrawScope.drawLogo(S: Float) {
    val cx = S / 2; val cy = S / 2; val R = S / 2 - S * 0.06f
    drawCircle(Color(0xFF080D1A), R, Offset(cx, cy))
    val rot = (25.0 * PI / 180).toFloat()
    val mr  = R * 0.52f
    val mcx = cx - mr * 0.06f * sin(rot); val mcy = cy + mr * 0.06f * cos(rot)
    val nx  = sin(rot); val ny = -cos(rot)
    drawCircle(Color(0xFF78A8FF), mr, Offset(mcx, mcy))
    drawCircle(Color(0xFF080D1A), mr * 0.80f, Offset(mcx + nx * mr * 0.72f, mcy + ny * mr * 0.72f))
    fun star(scx: Float, scy: Float, r: Float): Path {
        val p = Path(); val ri = r * 0.42f
        for (i in 0 until 10) {
            val a  = (-90.0 + i * 36.0) * PI / 180
            val rr = if (i % 2 == 0) r else ri
            val x  = scx + rr * cos(a).toFloat(); val y = scy + rr * sin(a).toFloat()
            if (i == 0) p.moveTo(x, y) else p.lineTo(x, y)
        }; p.close(); return p
    }
    val px = cos(rot); val py = sin(rot); val mg = mr * 0.095f * 2.8f
    drawPath(star(mcx + nx * mr * 0.32f + px * mr * 0.15f, mcy + ny * mr * 0.32f + py * mr * 0.15f, mr * 0.145f), Color(0xFFDCECFF).copy(.88f))
    drawPath(star(mcx + nx * (mr + mg) + px * mr * 0.38f,  mcy + ny * (mr + mg) + py * mr * 0.38f,  mr * 0.095f), Color(0xFFD2E4FF).copy(.75f))
    drawPath(star(mcx + nx * (mr + mg * 1.5f) - px * mr * 0.12f, mcy + ny * (mr + mg * 1.5f) - py * mr * 0.12f, mr * 0.07f), Color(0xFFC8DCFF).copy(.62f))
}

// ── Live clock ────────────────────────────────────────────────────────────────

@Composable
private fun LiveClock() {
    val c = LocalAppColors.current
    var h       by remember { mutableStateOf(java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)) }
    var m       by remember { mutableStateOf(java.util.Calendar.getInstance().get(java.util.Calendar.MINUTE)) }
    var secs    by remember { mutableStateOf(java.util.Calendar.getInstance().get(java.util.Calendar.SECOND)) }
    var dateStr by remember { mutableStateOf(java.text.SimpleDateFormat("EEEE d MMMM", java.util.Locale("es", "ES")).format(java.util.Date())) }
    LaunchedEffect(Unit) {
        while (true) {
            val cal = java.util.Calendar.getInstance()
            h       = cal.get(java.util.Calendar.HOUR_OF_DAY)
            m       = cal.get(java.util.Calendar.MINUTE)
            secs    = cal.get(java.util.Calendar.SECOND)
            dateStr = java.text.SimpleDateFormat("EEEE d MMMM", java.util.Locale("es", "ES")).format(java.util.Date())
            delay(1000)
        }
    }
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("${AlarmScheduler.pad(h)}:${AlarmScheduler.pad(m)}", color = c.txt,  fontSize = 44.sp, fontWeight = FontWeight.Light,   letterSpacing = 3.sp)
        Text(":${AlarmScheduler.pad(secs)}",                       color = c.txt3, fontSize = 13.sp)
        Text(dateStr,                                              color = c.txt3, fontSize = 11.sp)
    }
}

// ── Wake time picker ──────────────────────────────────────────────────────────

@Composable
private fun WakePicker(h: Int, m: Int, onHour: (Int) -> Unit, onMin: (Int) -> Unit) {
    val c = LocalAppColors.current
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text("HORA PARA DESPERTAR", color = c.txt3, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
        Spacer(Modifier.height(6.dp))
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment     = Alignment.CenterVertically,
        ) {
            TimeCol(h, { onHour(1) },  { onHour(-1) })
            Text(":", color = c.acc, fontSize = 40.sp, fontWeight = FontWeight.Light, modifier = Modifier.padding(horizontal = 6.dp))
            TimeCol(m, { onMin(5) }, { onMin(-5) })
        }
    }
}

@Composable
private fun TimeCol(v: Int, up: () -> Unit, dn: () -> Unit) {
    val c = LocalAppColors.current
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(up, modifier = Modifier.size(36.dp)) {
            Icon(Icons.Default.KeyboardArrowUp,   contentDescription = null, tint = c.acc, modifier = Modifier.size(20.dp))
        }
        Text(AlarmScheduler.pad(v), color = c.txt, fontSize = 40.sp, fontWeight = FontWeight.Light,
            modifier = Modifier.width(58.dp), textAlign = TextAlign.Center)
        IconButton(dn, modifier = Modifier.size(36.dp)) {
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = c.acc, modifier = Modifier.size(20.dp))
        }
    }
}

// ── Onset card ────────────────────────────────────────────────────────────────

@Composable
private fun OnsetCard(onset: Int, onOnset: (Int) -> Unit) {
    val c = LocalAppColors.current
    DbCard {
        Row(
            modifier          = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) {
                Text("TIEMPO PARA DORMIRTE", color = c.txt3, fontSize = 8.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Spacer(Modifier.height(2.dp))
                Text("$onset min", color = c.acc, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        Slider(
            value         = onset.toFloat(),
            onValueChange = { onOnset(it.toInt()) },
            valueRange    = 5f..60f,
            modifier      = Modifier.height(28.dp),
            colors        = SliderDefaults.colors(
                thumbColor         = c.acc,
                activeTrackColor   = c.acc,
                inactiveTrackColor = c.card2,
            ),
        )
    }
}

// ── Alarm status ──────────────────────────────────────────────────────────────

@Composable
private fun AlarmStatusCard(a: AlarmState, onCancel: () -> Unit, onCancelBkp: () -> Unit) {
    val c = LocalAppColors.current
    if (!a.isSet) {
        Surface(color = c.card2, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
            Text(
                "Sin alarma programada — pulsa \"Buenas noches\"",
                color    = c.txt3,
                fontSize = 12.sp,
                modifier = Modifier.padding(12.dp),
                textAlign = TextAlign.Center,
            )
        }
        return
    }
    Surface(
        color    = c.grn.copy(.07f),
        shape    = RoundedCornerShape(12.dp),
        border   = BorderStroke(1.dp, c.grn.copy(.2f)),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("⏰", fontSize = 20.sp)
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("${AlarmScheduler.pad(a.h1)}:${AlarmScheduler.pad(a.m1)}", color = c.grn, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                    Text(" · ", color = c.txt3, fontSize = 12.sp)
                    Text("${AlarmScheduler.pad(a.h2)}:${AlarmScheduler.pad(a.m2)} +5", color = c.txt2, fontSize = 15.sp)
                }
                Text("${a.cycles} ciclos · ${a.hoursText}", color = c.txt2, fontSize = 10.sp)
                Spacer(Modifier.height(4.dp))
                TextButton(
                    onClick        = onCancelBkp,
                    contentPadding = PaddingValues(0.dp),
                    modifier       = Modifier.height(22.dp),
                ) {
                    Text("✕ Cancelar recordatorio +5 min", color = c.red.copy(.7f), fontSize = 9.sp)
                }
            }
            IconButton(onCancel) {
                Icon(Icons.Default.Close, contentDescription = null, tint = c.red.copy(.6f), modifier = Modifier.size(18.dp))
            }
        }
    }
}

// ── MIUI setup guide card ─────────────────────────────────────────────────────

@Composable
private fun MiuiGuideCard(onShowGuide: () -> Unit) {
    val c = LocalAppColors.current
    TextButton(
        onClick        = onShowGuide,
        modifier       = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 2.dp),
    ) {
        Text("⚙️ Xiaomi: activar pantalla de bloqueo", color = c.txt3, fontSize = 10.sp)
    }
}

// ── Moon button ───────────────────────────────────────────────────────────────

@Composable
private fun MoonButton(onClick: () -> Unit) {
    val c = LocalAppColors.current
    Box(Modifier.fillMaxWidth(), Alignment.Center) {
        Surface(
            onClick          = onClick,
            shape            = CircleShape,
            color            = c.bg,
            border           = BorderStroke(2.dp, c.acc.copy(.5f)),
            modifier         = Modifier.size(120.dp),
            shadowElevation  = 8.dp,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier         = Modifier.fillMaxSize().background(
                    Brush.radialGradient(
                        listOf(c.card2.copy(.8f), c.bg),
                        Offset(40f, 30f),
                        160f,
                    )
                ),
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Canvas(Modifier.size(40.dp)) { drawLogo(size.minDimension) }
                    Spacer(Modifier.height(4.dp))
                    Text("Buenas noches",   color = c.txt,  fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Calcular alarmas", color = c.txt2, fontSize = 9.sp)
                }
            }
        }
    }
}

// ── Alarm modal ───────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlarmModal(
    options:   List<AlarmOption>,
    selected:  AlarmOption?,
    onPick:    (AlarmOption) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    fmt:       (Int) -> String,
) {
    val c = LocalAppColors.current
    ModalBottomSheet(onDismissRequest = onDismiss, containerColor = c.navBg) {
        Column(Modifier.padding(horizontal = 16.dp).padding(bottom = 32.dp)) {
            Text("🌙 Alarmas recomendadas", color = c.txt, fontSize = 18.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            Spacer(Modifier.height(16.dp))
            options.forEach { opt ->
                val isSel = opt == selected
                val bkpH  = (opt.wakeH * 60 + opt.wakeM + 5) / 60 % 24
                val bkpM  = (opt.wakeH * 60 + opt.wakeM + 5) % 60
                val dstr  = when {
                    opt.diffMin == 0 -> "Exacto"
                    opt.diffMin < 0  -> "${-opt.diffMin} min antes"
                    else             -> "+${opt.diffMin} min"
                }
                Surface(
                    onClick  = { onPick(opt) },
                    color    = if (opt.isBest) c.grn.copy(.05f) else c.card,
                    shape    = RoundedCornerShape(14.dp),
                    border   = BorderStroke(
                        width = if (isSel) 2.dp else 1.dp,
                        color = when { isSel -> c.grn; opt.isBest -> c.grn.copy(.35f); else -> c.acc.copy(.18f) },
                    ),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                ) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "${AlarmScheduler.pad(opt.wakeH)}:${AlarmScheduler.pad(opt.wakeM)}",
                                    color      = if (opt.isBest) c.grn else c.acc,
                                    fontSize   = 28.sp,
                                    fontWeight = FontWeight.Light,
                                )
                                Spacer(Modifier.width(8.dp))
                                if (opt.isBest) {
                                    Surface(color = c.grn.copy(.15f), shape = RoundedCornerShape(20.dp)) {
                                        Text(
                                            "✓ Recomendada",
                                            color    = c.grn,
                                            fontSize = 9.sp,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        )
                                    }
                                }
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text("🔄 ${opt.cycles} ciclos", color = c.txt2, fontSize = 11.sp)
                                Text("🕐 ${fmt(opt.totalMin)}",  color = c.txt2, fontSize = 11.sp)
                                Text(dstr,                       color = c.txt3, fontSize = 11.sp)
                            }
                            Text(
                                "+5 min: ${AlarmScheduler.pad(bkpH)}:${AlarmScheduler.pad(bkpM)}",
                                color    = c.grn.copy(.7f),
                                fontSize = 10.sp,
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Button(
                onClick  = onConfirm,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape    = RoundedCornerShape(12.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = c.acc),
            ) {
                Text("⏰ Programar alarmas", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
            Spacer(Modifier.height(6.dp))
            OutlinedButton(onDismiss, Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                Text("Cerrar", color = c.txt2)
            }
        }
    }
}

// ── Shared card ───────────────────────────────────────────────────────────────

@Composable
private fun DbCard(
    modifier: Modifier = Modifier.fillMaxWidth(),
    content:  @Composable ColumnScope.() -> Unit,
) {
    val c = LocalAppColors.current
    Card(
        colors   = CardDefaults.cardColors(containerColor = c.card),
        shape    = RoundedCornerShape(14.dp),
        border   = BorderStroke(1.dp, c.bdr),
        modifier = modifier,
    ) {
        Column(Modifier.padding(10.dp), content = content)
    }
}
