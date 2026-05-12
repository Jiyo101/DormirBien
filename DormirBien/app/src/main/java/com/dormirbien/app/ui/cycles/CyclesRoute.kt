package com.dormirbien.app.ui.cycles

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*

private val BG   = Color(0xFF070B14)
private val CARD = Color(0xFF0F1826)
private val ACC  = Color(0xFF5B7FFF)
private val ACC2 = Color(0xFF9D7BFF)
private val TXT  = Color(0xFFDCE8FF)
private val TXT2 = Color(0xFF7A92B8)
private val TXT3 = Color(0xFF3A4F6E)

@Composable
fun CyclesRoute() {
    Column(Modifier.fillMaxSize().background(BG).verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Ciclos de sueño", color = TXT, fontSize = 22.sp, fontWeight = FontWeight.Bold)

        Surface(color = ACC.copy(.08f), shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, ACC.copy(.18f)), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(14.dp)) {
                Text("¿Qué es un ciclo de sueño?", color = ACC, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text("Tu cerebro atraviesa repetidamente ciclos de ~90 minutos. Completarlos íntegros determina si te despiertas descansado o agotado.",
                    color = TXT2, fontSize = 12.sp, lineHeight = 18.sp)
            }
        }

        Text("FASES DEL SUEÑO", color = TXT3, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)

        listOf(
            Triple("N1 — Adormecimiento",   "Transición vigilia→sueño. Muy fácil despertar. 1-7 min.", Color(0xFF7A92B8)),
            Triple("N2 — Sueño ligero",      "Temperatura baja. Consolida la memoria. La fase más larga.", ACC),
            Triple("N3 — Sueño profundo",    "Restauración física: tejidos, inmunidad, crecimiento.", Color(0xFF3B82F6)),
            Triple("REM — Sueño paradójico", "Memoria a largo plazo, emociones, creatividad.", ACC2),
        ).forEach { (n, d, c) ->
            Surface(color = CARD, shape = RoundedCornerShape(11.dp),
                border = BorderStroke(1.dp, c.copy(.15f)), modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(12.dp)) {
                    Box(Modifier.width(4.dp).height(36.dp).background(c, RoundedCornerShape(2.dp)))
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text(n, color = TXT, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text(d, color = TXT2, fontSize = 11.sp, lineHeight = 16.sp)
                    }
                }
            }
        }

        Text("POR QUÉ NO ROMPER CICLOS", color = TXT3, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)

        listOf(
            Triple("⚡","Inercia del sueño","Interrumpir N3 causa aturdimiento severo que puede durar hasta 4 horas."),
            Triple("🧠","REM irrecuperable","Si el despertador corta un ciclo REM, ese procesamiento desaparece definitivamente."),
            Triple("📊","6h interrumpidas < 4.5h completas","Walker (Why We Sleep): 6 horas interrumpidas rinden peor que 4.5h en ciclos completos."),
            Triple("💡","Cómo calcula la app","Inicio real = ahora + tiempo de dormirte. Alarma = ciclo de 90 min más cercano a tu objetivo."),
        ).forEach { (icon, title, body) ->
            Surface(color = CARD, shape = RoundedCornerShape(13.dp),
                border = BorderStroke(1.dp, TXT3.copy(.12f)), modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(12.dp)) {
                    Text(icon, fontSize = 20.sp); Spacer(Modifier.width(10.dp))
                    Column {
                        Text(title, color = TXT, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(3.dp))
                        Text(body, color = TXT2, fontSize = 11.sp, lineHeight = 16.sp)
                    }
                }
            }
        }
    }
}
