package com.dormirbien.app.ui.home

import android.net.Uri
import androidx.compose.animation.core.*
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
import com.dormirbien.app.data.local.AlarmPreferences
import com.dormirbien.app.data.local.AlarmState
import kotlinx.coroutines.delay
import kotlin.math.*
import kotlin.random.Random

// ── Design tokens ─────────────────────────────────────────────────────────────
private val BG   = Color(0xFF070B14)
private val CARD = Color(0xFF0F1826)
private val CARD2= Color(0xFF162035)
private val ACC  = Color(0xFF5B7FFF)
private val ACC2 = Color(0xFF9D7BFF)
private val GRN  = Color(0xFF3ECF8E)
private val RED  = Color(0xFFFF6B6B)
private val TXT  = Color(0xFFDCE8FF)
private val TXT2 = Color(0xFF7A92B8)
private val TXT3 = Color(0xFF3A4F6E)
private val BDR  = Color(0xFF5B7FFF).copy(alpha = 0.12f)

// ── Route ─────────────────────────────────────────────────────────────────────

@Composable
fun HomeRoute(
    prefs:             AlarmPreferences,
    onScheduleAlarms:  (Int, Int, Int, String) -> Unit,
    onCancelAlarms:    () -> Unit,
    onCancelBackup:    () -> Unit,
    onOpenSoundPicker: ((Uri) -> Unit) -> Unit,
    onShowMiuiGuide:   () -> Unit = {},
    vm:                HomeViewModel = hiltViewModel(),
) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    if (state is HomeUiState.Loading) {
        Box(Modifier.fillMaxSize().background(BG), Alignment.Center) {
            CircularProgressIndicator(color = ACC)
        }
        return
    }
    val s = state as HomeUiState.Success

    if (s.showModal) {
        AlarmModal(
            options  = s.options,
            selected = s.selected,
            onPick   = { vm.onAction(HomeAction.Pick(it)) },
            onConfirm = {
                val opt = s.selected ?: return@AlarmModal
                onScheduleAlarms(opt.wakeH, opt.wakeM, opt.cycles, vm.fmtH(opt.totalMin))
                vm.onAction(HomeAction.CloseModal)
            },
            onDismiss = { vm.onAction(HomeAction.CloseModal) },
            fmt = vm::fmtH,
        )
    }

    HomeScreen(
        s               = s,
        onAction        = vm::onAction,
        onCancelAlarms  = onCancelAlarms,
        onCancelBackup  = onCancelBackup,
        onOpenSoundPicker = onOpenSoundPicker,
        onOpenModal     = { vm.onAction(HomeAction.OpenModal) },
        onShowMiuiGuide = onShowMiuiGuide,
    )
}

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
private fun HomeScreen(
    s:                 HomeUiState.Success,
    onAction:          (HomeAction) -> Unit,
    onCancelAlarms:    () -> Unit,
    onCancelBackup:    () -> Unit,
    onOpenSoundPicker: ((Uri) -> Unit) -> Unit,
    onOpenModal:       () -> Unit,
    onShowMiuiGuide:   () -> Unit,
) {
    Column(
        Modifier.fillMaxSize().background(BG).padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Spacer(Modifier.height(12.dp))

        // Logo + clock — top section
        Column(horizontalAlignment = Alignment.CenterHorizontally,
               modifier = Modifier.fillMaxWidth()) {
            LogoRow()
            Spacer(Modifier.height(6.dp))
            LiveClock(s.nowH, s.nowM)
        }

        // Wake time picker
        WakePicker(s.wakeH, s.wakeM,
            onHour = { onAction(HomeAction.HourDelta(it)) },
            onMin  = { onAction(HomeAction.MinuteDelta(it)) })

        // Settings row: onset + sound in one line
        SettingsRow(
            onset          = s.onset,
            onOnset        = { onAction(HomeAction.SetOnset(it)) },
            onPickSound    = onOpenSoundPicker,
        )

        // Alarm status
        AlarmStatusCard(s.alarm, onCancelAlarms, onCancelBackup)

        // Moon button + guide at bottom
        Column(horizontalAlignment = Alignment.CenterHorizontally,
               modifier = Modifier.fillMaxWidth()) {
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
    Row(
        Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment     = Alignment.CenterVertically,
    ) {
        Canvas(Modifier.size(36.dp)) { drawLogo(size.minDimension) }
        Spacer(Modifier.width(10.dp))
        Text("Dormir", color = TXT,  fontSize = 20.sp, fontWeight = FontWeight.Light,  letterSpacing = 2.sp)
        Text("Bien",   color = ACC,  fontSize = 20.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 2.sp)
    }
}

private fun DrawScope.drawLogo(S: Float) {
    val cx = S/2; val cy = S/2; val R = S/2 - S*0.06f
    drawCircle(Color(0xFF080D1A), R, Offset(cx,cy))
    val rot = (25.0 * PI / 180).toFloat()
    val mr = R*0.52f
    val mcx = cx - mr*0.06f* sin(rot); val mcy = cy + mr*0.06f* cos(rot)
    val nx = sin(rot); val ny = -cos(rot)
    drawCircle(Color(0xFF78A8FF), mr, Offset(mcx,mcy))
    drawCircle(Color(0xFF080D1A), mr*0.80f, Offset(mcx+nx*mr*0.72f, mcy+ny*mr*0.72f))
    fun star(scx: Float, scy: Float, r: Float): Path {
        val p = Path(); val ri = r*0.42f
        for (i in 0 until 10) {
            val a = (-90.0+i*36.0) * PI/180
            val rr = if (i%2==0) r else ri
            val x = scx + rr* cos(a).toFloat(); val y = scy + rr* sin(a).toFloat()
            if (i==0) p.moveTo(x,y) else p.lineTo(x,y)
        }; p.close(); return p
    }
    val px = cos(rot); val py = sin(rot); val mg = mr*0.095f*2.8f
    drawPath(star(mcx+nx*mr*0.32f+px*mr*0.15f, mcy+ny*mr*0.32f+py*mr*0.15f, mr*0.145f), Color(0xFFDCECFF).copy(.88f))
    drawPath(star(mcx+nx*(mr+mg)+px*mr*0.38f,  mcy+ny*(mr+mg)+py*mr*0.38f,  mr*0.095f), Color(0xFFD2E4FF).copy(.75f))
    drawPath(star(mcx+nx*(mr+mg*1.5f)-px*mr*0.12f, mcy+ny*(mr+mg*1.5f)-py*mr*0.12f, mr*0.07f), Color(0xFFC8DCFF).copy(.62f))
}

// ── Live clock ────────────────────────────────────────────────────────────────

@Composable
private fun LiveClock(h: Int, m: Int) {
    var secs by remember { mutableStateOf(0) }
    var dateStr by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        while (true) {
            val c = java.util.Calendar.getInstance()
            secs = c.get(java.util.Calendar.SECOND)
            dateStr = java.text.SimpleDateFormat("EEEE d MMMM", java.util.Locale("es","ES")).format(java.util.Date())
            delay(1000)
        }
    }
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("${AlarmScheduler.pad(h)}:${AlarmScheduler.pad(m)}", color = TXT, fontSize = 44.sp, fontWeight = FontWeight.Light, letterSpacing = 3.sp)
        Text(":${AlarmScheduler.pad(secs)}", color = TXT3, fontSize = 13.sp)
        Text(dateStr, color = TXT3, fontSize = 11.sp)
    }
}

// ── Wake time picker ──────────────────────────────────────────────────────────

@Composable
private fun WakePicker(h: Int, m: Int, onHour: (Int) -> Unit, onMin: (Int) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
           modifier = Modifier.fillMaxWidth()) {
        Text("HORA PARA DESPERTAR", color = TXT3, fontSize = 9.sp,
            fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
        Spacer(Modifier.height(6.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            TimeCol(h, { onHour(1) },  { onHour(-1) })
            Text(":", color = ACC, fontSize = 40.sp, fontWeight = FontWeight.Light,
                modifier = Modifier.padding(horizontal = 6.dp))
            TimeCol(m, { onMin(5) }, { onMin(-5) })
        }
    }
}

@Composable
private fun TimeCol(v: Int, up: () -> Unit, dn: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(up, modifier = Modifier.size(36.dp)) {
            Icon(Icons.Default.KeyboardArrowUp, null, tint = ACC, modifier = Modifier.size(20.dp))
        }
        Text(AlarmScheduler.pad(v), color = TXT, fontSize = 40.sp,
            fontWeight = FontWeight.Light, modifier = Modifier.width(58.dp),
            textAlign = TextAlign.Center)
        IconButton(dn, modifier = Modifier.size(36.dp)) {
            Icon(Icons.Default.KeyboardArrowDown, null, tint = ACC, modifier = Modifier.size(20.dp))
        }
    }
}

// ── Onset slider ──────────────────────────────────────────────────────────────

@Composable
private fun OnsetSlider(v: Int, onChange: (Int) -> Unit) {
    // kept for compatibility — not used directly in new layout
}

// ── Sound row ─────────────────────────────────────────────────────────────────

@Composable
private fun SoundRow(onPick: ((Uri) -> Unit) -> Unit) {
    // kept for compatibility — not used directly in new layout
}

// ── Combined settings row (onset + sound) ────────────────────────────────────

@Composable
private fun SettingsRow(
    onset:       Int,
    onOnset:     (Int) -> Unit,
    onPickSound: ((Uri) -> Unit) -> Unit,
) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        // Onset compact
        DbCard(modifier = Modifier.weight(1f)) {
            Text("DORMIRTE", color = TXT3, fontSize = 8.sp,
                fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Spacer(Modifier.height(4.dp))
            Text("$onset min", color = ACC, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Slider(
                value         = onset.toFloat(),
                onValueChange = { onOnset(it.toInt()) },
                valueRange    = 5f..60f,
                modifier      = Modifier.height(28.dp),
                colors = SliderDefaults.colors(
                    thumbColor = ACC, activeTrackColor = ACC, inactiveTrackColor = CARD2)
            )
        }
        // Sound compact
        DbCard(modifier = Modifier.weight(1f)) {
            Text("SONIDO", color = TXT3, fontSize = 8.sp,
                fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Spacer(Modifier.height(4.dp))
            Text("Alarma", color = TXT2, fontSize = 12.sp)
            Spacer(Modifier.height(6.dp))
            Button(
                onClick        = { onPickSound {} },
                colors         = ButtonDefaults.buttonColors(containerColor = CARD2),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier       = Modifier.fillMaxWidth(),
            ) {
                Text("Cambiar", color = ACC, fontSize = 11.sp)
            }
        }
    }
}

// ── Alarm status ──────────────────────────────────────────────────────────────

@Composable
private fun AlarmStatusCard(a: AlarmState, onCancel: () -> Unit, onCancelBkp: () -> Unit) {
    if (!a.isSet) {
        Surface(color = CARD2, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
            Text("Sin alarma programada — pulsa \"Buenas noches\"", color = TXT3, fontSize = 12.sp,
                modifier = Modifier.padding(12.dp), textAlign = TextAlign.Center)
        }
        return
    }
    Surface(color = GRN.copy(.07f), shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, GRN.copy(.2f)), modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("⏰", fontSize = 20.sp)
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("${AlarmScheduler.pad(a.h1)}:${AlarmScheduler.pad(a.m1)}", color = GRN, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                    Text(" · ", color = TXT3, fontSize = 12.sp)
                    Text("${AlarmScheduler.pad(a.h2)}:${AlarmScheduler.pad(a.m2)} +5", color = TXT2, fontSize = 15.sp)
                }
                Text("${a.cycles} ciclos · ${a.hoursText}", color = TXT2, fontSize = 10.sp)
                Spacer(Modifier.height(4.dp))
                TextButton(onClick = onCancelBkp, contentPadding = PaddingValues(0.dp), modifier = Modifier.height(22.dp)) {
                    Text("✕ Cancelar recordatorio +5 min", color = RED.copy(.7f), fontSize = 9.sp)
                }
            }
            IconButton(onCancel) { Icon(Icons.Default.Close, null, tint = RED.copy(.6f), modifier = Modifier.size(18.dp)) }
        }
    }
}


// ── MIUI setup guide card ─────────────────────────────────────────────────────

@Composable
private fun MiuiGuideCard(onShowGuide: () -> Unit) {
    TextButton(
        onClick  = onShowGuide,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 2.dp),
    ) {
        Text("⚙️ Xiaomi: activar pantalla de bloqueo",
            color = TXT3, fontSize = 10.sp)
    }
}

// ── Moon button ───────────────────────────────────────────────────────────────

@Composable
private fun MoonButton(onClick: () -> Unit) {
    val pulse by rememberInfiniteTransition(label = "moon").animateFloat(
        0.18f, 0.28f, infiniteRepeatable(tween(2000), RepeatMode.Reverse), label = "p")
    Box(Modifier.fillMaxWidth(), Alignment.Center) {
        Surface(onClick = onClick, shape = CircleShape, color = BG,
            border = BorderStroke(2.dp, ACC.copy(.5f)), modifier = Modifier.size(120.dp), shadowElevation = 8.dp) {
            Box(Modifier.fillMaxSize().background(
                Brush.radialGradient(listOf(Color(0xFF1A2560).copy(.8f), BG), Offset(40f,30f), 160f)
            ), Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Canvas(Modifier.size(40.dp)) { drawLogo(size.minDimension) }
                    Spacer(Modifier.height(4.dp))
                    Text("Buenas noches", color = TXT, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Calcular alarmas", color = TXT2, fontSize = 9.sp)
                }
            }
        }
    }
}

// ── Alarm modal ───────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlarmModal(
    options: List<AlarmOption>, selected: AlarmOption?,
    onPick: (AlarmOption) -> Unit, onConfirm: () -> Unit, onDismiss: () -> Unit,
    fmt: (Int) -> String,
) {
    ModalBottomSheet(onDismissRequest = onDismiss, containerColor = Color(0xFF0C1220)) {
        Column(Modifier.padding(horizontal = 16.dp).padding(bottom = 32.dp)) {
            Text("🌙 Alarmas recomendadas", color = TXT, fontSize = 18.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            Spacer(Modifier.height(16.dp))
            options.forEach { opt ->
                val isSel = opt == selected
                val bkpH  = (opt.wakeH * 60 + opt.wakeM + 5) / 60 % 24
                val bkpM  = (opt.wakeH * 60 + opt.wakeM + 5) % 60
                val dstr  = when { opt.diffMin == 0 -> "Exacto"; opt.diffMin < 0 -> "${-opt.diffMin} min antes"; else -> "+${opt.diffMin} min" }
                Surface(onClick = { onPick(opt) },
                    color  = if (opt.isBest) GRN.copy(.05f) else Color(0xFF0F1826),
                    shape  = RoundedCornerShape(14.dp),
                    border = BorderStroke(if (isSel) 2.dp else 1.dp,
                        if (isSel) GRN else if (opt.isBest) GRN.copy(.35f) else ACC.copy(.18f)),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("${AlarmScheduler.pad(opt.wakeH)}:${AlarmScheduler.pad(opt.wakeM)}",
                                    color = if (opt.isBest) GRN else ACC, fontSize = 28.sp, fontWeight = FontWeight.Light)
                                Spacer(Modifier.width(8.dp))
                                if (opt.isBest) Surface(color = GRN.copy(.15f), shape = RoundedCornerShape(20.dp)) {
                                    Text("✓ Recomendada", color = GRN, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                                }
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text("🔄 ${opt.cycles} ciclos", color = TXT2, fontSize = 11.sp)
                                Text("🕐 ${fmt(opt.totalMin)}", color = TXT2, fontSize = 11.sp)
                                Text(dstr, color = TXT3, fontSize = 11.sp)
                            }
                            Text("+5 min: ${AlarmScheduler.pad(bkpH)}:${AlarmScheduler.pad(bkpM)}",
                                color = GRN.copy(.7f), fontSize = 10.sp)
                        }
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Button(onConfirm, Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ACC)) {
                Text("⏰ Programar alarmas", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
            Spacer(Modifier.height(6.dp))
            OutlinedButton(onDismiss, Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                Text("Cerrar", color = TXT2)
            }
        }
    }
}

// ── Shared card ───────────────────────────────────────────────────────────────

@Composable
private fun DbCard(modifier: Modifier = Modifier.fillMaxWidth(),
                   content: @Composable ColumnScope.() -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = CARD),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, BDR),
        modifier = modifier) {
        Column(Modifier.padding(10.dp), content = content)
    }
}
