package com.dormirbien.app.ui.ajustes

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dormirbien.app.ui.theme.LocalAppColors

@Composable
fun AjustesRoute(
    onOpenSoundPicker: ((Uri) -> Unit) -> Unit,
    vm: AjustesViewModel = hiltViewModel(),
) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    val c = LocalAppColors.current
    if (state is AjustesUiState.Loading) {
        Box(Modifier.fillMaxSize().background(c.bg), Alignment.Center) {
            CircularProgressIndicator(color = c.acc)
        }
        return
    }
    val s = state as AjustesUiState.Success
    AjustesScreen(
        data             = s.data,
        onToggleDarkMode = vm::toggleDarkMode,
        onPickSound      = { onOpenSoundPicker {} },
    )
}

@Composable
private fun AjustesScreen(
    data:             AjustesUiData,
    onToggleDarkMode: (Boolean) -> Unit,
    onPickSound:      () -> Unit,
) {
    val c = LocalAppColors.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(c.bg)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Spacer(Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Settings, contentDescription = null, tint = c.acc, modifier = Modifier.size(28.dp))
            Spacer(Modifier.width(10.dp))
            Column {
                Text("Ajustes", color = c.txt, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("Preferencias de la app", color = c.txt3, fontSize = 12.sp)
            }
        }

        SectionLabel("Apariencia")

        SettingCard {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector        = if (data.modoOscuroActivado) Icons.Default.DarkMode else Icons.Default.LightMode,
                        contentDescription = null,
                        tint               = c.acc,
                        modifier           = Modifier.size(22.dp),
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Modo oscuro", color = c.txt, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                        Text(
                            if (data.modoOscuroActivado) "Activado" else "Desactivado",
                            color    = c.txt3,
                            fontSize = 12.sp,
                        )
                    }
                }
                Switch(
                    checked         = data.modoOscuroActivado,
                    onCheckedChange = onToggleDarkMode,
                    colors          = SwitchDefaults.colors(
                        checkedThumbColor   = c.card,
                        checkedTrackColor   = c.acc,
                        uncheckedThumbColor = c.card,
                        uncheckedTrackColor = c.txt3,
                    ),
                )
            }
        }

        SectionLabel("Sonido de alarma")

        SettingCard {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier          = Modifier.weight(1f),
                ) {
                    Icon(Icons.Default.MusicNote, contentDescription = null, tint = c.acc, modifier = Modifier.size(22.dp))
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Sonido de alarma", color = c.txt, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                        Text(
                            if (data.sonidoAlarma.isNotEmpty()) "Personalizado" else "Por defecto",
                            color    = c.txt3,
                            fontSize = 12.sp,
                        )
                    }
                }
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick        = onPickSound,
                    colors         = ButtonDefaults.buttonColors(containerColor = c.card2),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    shape          = RoundedCornerShape(10.dp),
                ) {
                    Text("Cambiar", color = c.acc, fontSize = 13.sp)
                }
            }
        }

        if (data.sonidoAlarma.isNotEmpty()) {
            Text("✓ Sonido personalizado guardado", color = c.grn, fontSize = 11.sp)
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun SectionLabel(text: String) {
    val c = LocalAppColors.current
    Text(
        text.uppercase(),
        color         = c.txt3,
        fontSize      = 10.sp,
        fontWeight    = FontWeight.Bold,
        letterSpacing = 1.sp,
    )
}

@Composable
private fun SettingCard(content: @Composable () -> Unit) {
    val c = LocalAppColors.current
    Card(
        colors   = CardDefaults.cardColors(containerColor = c.card),
        shape    = RoundedCornerShape(14.dp),
        border   = BorderStroke(1.dp, c.bdr),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Box(Modifier.padding(16.dp)) { content() }
    }
}
