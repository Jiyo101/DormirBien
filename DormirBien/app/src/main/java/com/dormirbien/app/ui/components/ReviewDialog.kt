package com.dormirbien.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import com.dormirbien.app.ui.theme.LocalAppColors
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewDialog(onDismiss: () -> Unit, onSave: (stars: Int, feeling: String) -> Unit) {
    val c       = LocalAppColors.current
    var stars   by remember { mutableStateOf(0) }
    var feeling by remember { mutableStateOf("") }
    val date = remember { SimpleDateFormat("EEEE d MMM", Locale("es", "ES")).format(Date()) }

    ModalBottomSheet(onDismissRequest = onDismiss, containerColor = c.navBg) {
        Column(
            modifier              = Modifier.padding(horizontal = 16.dp).padding(bottom = 32.dp),
            horizontalAlignment   = Alignment.CenterHorizontally,
        ) {
            Text("☀️ ¿Cómo has dormido?", color = c.txt, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(date, color = c.txt2, fontSize = 12.sp)
            Spacer(Modifier.height(16.dp))
            Text("Puntuación general", color = c.txt3, fontSize = 10.sp)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                (1..5).forEach { n ->
                    TextButton(onClick = { stars = n }, contentPadding = PaddingValues(0.dp)) {
                        Text(
                            "⭐",
                            fontSize = 28.sp,
                            color    = if (n <= stars) Color.White else Color.White.copy(.18f),
                        )
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Text("¿Cómo te has despertado?", color = c.txt3, fontSize = 10.sp)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf(
                    "😵" to "Agotado",
                    "😴" to "Cansado",
                    "😐" to "Normal",
                    "😊" to "Bien",
                    "🚀" to "Genial",
                ).forEach { (emoji, label) ->
                    val selected = feeling == "$emoji $label"
                    Surface(
                        onClick  = { feeling = "$emoji $label" },
                        color    = if (selected) c.acc.copy(.12f) else c.card2,
                        shape    = RoundedCornerShape(10.dp),
                        border   = BorderStroke(
                            width = if (selected) 1.5.dp else 1.dp,
                            color = if (selected) c.acc else c.txt3.copy(.3f),
                        ),
                        modifier = Modifier.weight(1f),
                    ) {
                        Column(
                            modifier            = Modifier.padding(vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(emoji, fontSize = 18.sp)
                            Text(
                                label,
                                color      = if (selected) c.acc else c.txt3,
                                fontSize   = 9.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick  = { onSave(stars, feeling); onDismiss() },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape    = RoundedCornerShape(12.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = c.acc),
            ) {
                Text("Guardar valoración", fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(6.dp))
            OutlinedButton(onDismiss, Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                Text("Ahora no", color = c.txt2)
            }
        }
    }
}
