package com.dormirbien.app.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private fun darkScheme() = darkColorScheme(
    primary      = Color(0xFF5B7FFF),
    onPrimary    = Color(0xFF070B14),
    background   = Color(0xFF070B14),
    surface      = Color(0xFF0F1826),
    onBackground = Color(0xFFDCE8FF),
    onSurface    = Color(0xFFDCE8FF),
)

private fun lightScheme() = lightColorScheme(
    primary      = Color(0xFF3A5FD9),
    onPrimary    = Color(0xFFFFFFFF),
    background   = Color(0xFFF4F6FF),
    surface      = Color(0xFFFFFFFF),
    onBackground = Color(0xFF0D1435),
    onSurface    = Color(0xFF0D1435),
)

@Composable
fun DormirBienTheme(darkTheme: Boolean = true, content: @Composable () -> Unit) {
    val colors = if (darkTheme) DarkAppColors else LightAppColors
    CompositionLocalProvider(LocalAppColors provides colors) {
        MaterialTheme(
            colorScheme = if (darkTheme) darkScheme() else lightScheme(),
            content     = content,
        )
    }
}
