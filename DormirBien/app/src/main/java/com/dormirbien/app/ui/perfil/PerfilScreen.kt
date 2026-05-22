package com.dormirbien.app.ui.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dormirbien.app.data.repository.Usuario
import com.dormirbien.app.ui.theme.LocalAppColors

@Composable
fun PerfilRoute(vm: PerfilViewModel = hiltViewModel()) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    val c = LocalAppColors.current
    if (state is PerfilUiState.Loading) {
        Box(Modifier.fillMaxSize().background(c.bg), Alignment.Center) {
            CircularProgressIndicator(color = c.acc)
        }
        return
    }
    val s = state as PerfilUiState.Success
    PerfilScreen(usuario = s.usuario, onSave = vm::save)
}

@Composable
private fun PerfilScreen(usuario: Usuario?, onSave: (String, Int, Float) -> Unit) {
    val c      = LocalAppColors.current
    var nombre by remember(usuario) { mutableStateOf(usuario?.nombre ?: "") }
    var edad   by remember(usuario) {
        mutableStateOf(if ((usuario?.edad ?: 0) > 0) usuario!!.edad.toString() else "")
    }
    var peso   by remember(usuario) {
        mutableStateOf(if ((usuario?.peso ?: 0f) > 0f) usuario!!.peso.toString() else "")
    }
    var saved  by remember { mutableStateOf(false) }

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
            Icon(Icons.Default.Person, contentDescription = null, tint = c.acc, modifier = Modifier.size(28.dp))
            Spacer(Modifier.width(10.dp))
            Column {
                Text("Perfil", color = c.txt, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("Datos personales", color = c.txt3, fontSize = 12.sp)
            }
        }

        FormField(
            label        = "Nombre",
            value        = nombre,
            onValue      = { nombre = it; saved = false },
            placeholder  = "Tu nombre",
            keyboardType = KeyboardType.Text,
        )

        FormField(
            label        = "Edad",
            value        = edad,
            onValue      = {
                if (it.length <= 3 && it.all(Char::isDigit)) { edad = it; saved = false }
            },
            placeholder  = "Años",
            keyboardType = KeyboardType.Number,
            suffix       = "años",
        )

        FormField(
            label        = "Peso",
            value        = peso,
            onValue      = {
                if (it.matches(Regex("\\d{0,3}(\\.\\d{0,1})?"))) { peso = it; saved = false }
            },
            placeholder  = "Kilogramos",
            keyboardType = KeyboardType.Decimal,
            suffix       = "kg",
        )

        Button(
            onClick  = {
                onSave(nombre.trim(), edad.toIntOrNull() ?: 0, peso.toFloatOrNull() ?: 0f)
                saved = true
            },
            enabled  = nombre.isNotBlank(),
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape    = RoundedCornerShape(14.dp),
            colors   = ButtonDefaults.buttonColors(containerColor = c.acc),
        ) {
            Text(
                if (saved) "✓ Guardado" else "Guardar perfil",
                fontWeight = FontWeight.Bold,
                fontSize   = 16.sp,
            )
        }

        if (saved) {
            Text(
                "Perfil actualizado correctamente",
                color    = c.grn,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun FormField(
    label:       String,
    value:       String,
    onValue:     (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    suffix:      String = "",
) {
    val c = LocalAppColors.current
    Column {
        Text(
            label.uppercase(),
            color         = c.txt3,
            fontSize      = 10.sp,
            fontWeight    = FontWeight.Bold,
            letterSpacing = 1.sp,
        )
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value           = value,
            onValueChange   = onValue,
            placeholder     = { Text(placeholder, color = c.txt3) },
            suffix          = if (suffix.isNotEmpty()) ({ Text(suffix, color = c.txt2) }) else null,
            singleLine      = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier        = Modifier.fillMaxWidth(),
            colors          = OutlinedTextFieldDefaults.colors(
                focusedBorderColor      = c.acc,
                unfocusedBorderColor    = c.bdr,
                focusedTextColor        = c.txt,
                unfocusedTextColor      = c.txt,
                cursorColor             = c.acc,
                focusedContainerColor   = c.card,
                unfocusedContainerColor = c.card,
            ),
            shape = RoundedCornerShape(12.dp),
        )
    }
}
