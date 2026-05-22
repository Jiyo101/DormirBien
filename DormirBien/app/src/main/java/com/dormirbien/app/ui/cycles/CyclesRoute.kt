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
import com.dormirbien.app.ui.theme.LocalAppColors

@Composable
fun CyclesRoute() {
    val c = LocalAppColors.current
    Column(
        Modifier
            .fillMaxSize()
            .background(c.bg)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text("Ciclos de sueño", color = c.txt, fontSize = 22.sp, fontWeight = FontWeight.Bold)

        Surface(
            color    = c.acc.copy(.08f),
            shape    = RoundedCornerShape(14.dp),
            border   = BorderStroke(1.dp, c.acc.copy(.18f)),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(Modifier.padding(14.dp)) {
                Text("¿Qué es un ciclo de sueño?", color = c.acc, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(
                    "Tu cerebro atraviesa repetidamente ciclos de ~90 minutos. Completarlos íntegros determina si te despiertas descansado o agotado.",
                    color      = c.txt2,
                    fontSize   = 12.sp,
                    lineHeight = 18.sp,
                )
            }
        }

        Text("FASES DEL SUEÑO", color = c.txt3, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)

        listOf(
            Triple("N1 — Adormecimiento",   "Transición vigilia→sueño. Muy fácil despertar. 1-7 min.",  c.txt2),
            Triple("N2 — Sueño ligero",      "Temperatura baja. Consolida la memoria. La fase más larga.", c.acc),
            Triple("N3 — Sueño profundo",    "Restauración física: tejidos, inmunidad, crecimiento.",      Color(0xFF3B82F6)),
            Triple("REM — Sueño paradójico", "Memoria a largo plazo, emociones, creatividad.",             c.acc2),
        ).forEach { (name, desc, color) ->
            Surface(
                color    = c.card,
                shape    = RoundedCornerShape(11.dp),
                border   = BorderStroke(1.dp, color.copy(.15f)),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(Modifier.padding(12.dp)) {
                    Box(Modifier.width(4.dp).height(36.dp).background(color, RoundedCornerShape(2.dp)))
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text(name, color = c.txt,  fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text(desc, color = c.txt2, fontSize = 11.sp, lineHeight = 16.sp)
                    }
                }
            }
        }

        Text("POR QUÉ NO ROMPER CICLOS", color = c.txt3, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)

        listOf(
            Triple("⚡", "Inercia del sueño",             "Interrumpir N3 causa aturdimiento severo que puede durar hasta 4 horas."),
            Triple("🧠", "REM irrecuperable",             "Si el despertador corta un ciclo REM, ese procesamiento desaparece definitivamente."),
            Triple("📊", "6h interrumpidas < 4.5h completas", "Walker (Why We Sleep): 6 horas interrumpidas rinden peor que 4.5h en ciclos completos."),
            Triple("💡", "Cómo calcula la app",           "Inicio real = ahora + tiempo de dormirte. Alarma = ciclo de 90 min más cercano a tu objetivo."),
        ).forEach { (icon, title, body) ->
            Surface(
                color    = c.card,
                shape    = RoundedCornerShape(13.dp),
                border   = BorderStroke(1.dp, c.txt3.copy(.12f)),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(Modifier.padding(12.dp)) {
                    Text(icon, fontSize = 20.sp)
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(title, color = c.txt,  fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(3.dp))
                        Text(body,  color = c.txt2, fontSize = 11.sp, lineHeight = 16.sp)
                    }
                }
            }
        }
    }
}
