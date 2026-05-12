package com.dormirbien.app.ui.tips

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
private val TXT  = Color(0xFFDCE8FF)
private val TXT2 = Color(0xFF7A92B8)
private val TXT3 = Color(0xFF3A4F6E)

private val TIPS = listOf(
    "ENTORNO" to listOf(
        Triple("🌡️","16-19 °C","El cuerpo necesita bajar su temperatura central ~1 °C para iniciar el sueño. Un dormitorio fresco acelera el proceso hasta un 30%."),
        Triple("🌑","Oscuridad total","Cualquier luz suprime la melatonina. Un antifaz puede reducir el tiempo de dormirse a la mitad."),
        Triple("🔇","Silencio o ruido blanco","El ruido blanco enmascara sonidos disruptivos mejorando la continuidad del sueño."),
    ),
    "HÁBITOS" to listOf(
        Triple("📱","Sin pantallas 1h antes","La luz azul suprime melatonina hasta 3 horas. Activa el modo nocturno si no puedes evitar el móvil."),
        Triple("☕","Última cafeína antes de las 14:00","Vida media ~5-6 horas. Un café a las 15h → la mitad aún activa a las 21h."),
        Triple("🍷","El alcohol destruye el REM","Aunque ayuda a dormirse, fragmenta el sueño y suprime el REM hasta un 24%."),
        Triple("🏃","Ejercicio regular","Aumenta el sueño profundo un 65%. Evítalo 2h antes de dormir."),
    ),
    "RITMO CIRCADIANO" to listOf(
        Triple("🔆","Luz solar al despertar (30 min)","Sincroniza el reloj circadiano y aumenta la serotonina durante todo el día."),
        Triple("📅","Misma hora todos los días","El factor más importante. El «jet lag social» del fin de semana es uno de los mayores disruptores modernos."),
        Triple("🧘","Respiración 4-7-8","Inhala 4s → aguanta 7s → exhala 8s. Reduce el cortisol y activa el sistema nervioso parasimpático."),
        Triple("🍌","Triptófano antes de dormir","Plátano, avena o kiwi. 2 kiwis 1h antes mejoran la calidad del sueño hasta un 42%."),
    ),
)

@Composable
fun TipsRoute() {
    Column(Modifier.fillMaxSize().background(BG).verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Higiene del sueño", color = TXT, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        TIPS.forEach { (section, tips) ->
            Text(section, color = TXT3, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
            tips.forEach { (icon, title, body) ->
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
        Spacer(Modifier.height(8.dp))
    }
}
