package com.dormirbien.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import java.text.SimpleDateFormat
import java.util.*

private val ACC = Color(0xFF5B7FFF)
private val TXT = Color(0xFFDCE8FF)
private val TXT2= Color(0xFF7A92B8)
private val TXT3= Color(0xFF3A4F6E)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewDialog(onDismiss: () -> Unit, onSave: (stars: Int, feeling: String) -> Unit) {
    var stars   by remember { mutableStateOf(0) }
    var feeling by remember { mutableStateOf("") }
    val date = remember { SimpleDateFormat("EEEE d MMM", Locale("es","ES")).format(Date()) }

    ModalBottomSheet(onDismissRequest = onDismiss, containerColor = Color(0xFF0C1220)) {
        Column(Modifier.padding(horizontal = 16.dp).padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text("☀️ ¿Cómo has dormido?", color = TXT, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(date, color = TXT2, fontSize = 12.sp)
            Spacer(Modifier.height(16.dp))
            Text("Puntuación general", color = TXT3, fontSize = 10.sp)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                (1..5).forEach { n ->
                    TextButton(onClick = { stars = n }, contentPadding = PaddingValues(0.dp)) {
                        Text("⭐", fontSize = 28.sp,
                            color = if (n <= stars) Color.White else Color.White.copy(.18f))
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Text("¿Cómo te has despertado?", color = TXT3, fontSize = 10.sp)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("😵" to "Agotado","😴" to "Cansado","😐" to "Normal","😊" to "Bien","🚀" to "Genial").forEach { (e, l) ->
                    val sel = feeling == "$e $l"
                    Surface(onClick = { feeling = "$e $l" },
                        color  = if (sel) ACC.copy(.12f) else Color(0xFF162035),
                        shape  = RoundedCornerShape(10.dp),
                        border = BorderStroke(if (sel) 1.5.dp else 1.dp, if (sel) ACC else TXT3.copy(.3f)),
                        modifier = Modifier.weight(1f)) {
                        Column(Modifier.padding(vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(e, fontSize = 18.sp)
                            Text(l, color = if (sel) ACC else TXT3, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Button(onClick = { onSave(stars, feeling); onDismiss() },
                modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ACC)) {
                Text("Guardar valoración", fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(6.dp))
            OutlinedButton(onDismiss, Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                Text("Ahora no", color = TXT2)
            }
        }
    }
}
