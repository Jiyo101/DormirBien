package com.dormirbien.app.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class AppColors(
    val bg:    Color,
    val card:  Color,
    val card2: Color,
    val acc:   Color,
    val acc2:  Color,
    val grn:   Color,
    val yel:   Color,
    val red:   Color,
    val txt:   Color,
    val txt2:  Color,
    val txt3:  Color,
    val bdr:   Color,
    val navBg: Color,
)

val DarkAppColors = AppColors(
    bg    = Color(0xFF070B14),
    card  = Color(0xFF0F1826),
    card2 = Color(0xFF162035),
    acc   = Color(0xFF5B7FFF),
    acc2  = Color(0xFF9D7BFF),
    grn   = Color(0xFF3ECF8E),
    yel   = Color(0xFFFFD166),
    red   = Color(0xFFFF6B6B),
    txt   = Color(0xFFDCE8FF),
    txt2  = Color(0xFF7A92B8),
    txt3  = Color(0xFF3A4F6E),
    bdr   = Color(0xFF5B7FFF).copy(alpha = 0.12f),
    navBg = Color(0xFF0C1220),
)

val LightAppColors = AppColors(
    bg    = Color(0xFFF4F6FF),
    card  = Color(0xFFFFFFFF),
    card2 = Color(0xFFEAF0FF),
    acc   = Color(0xFF3A5FD9),
    acc2  = Color(0xFF7057CC),
    grn   = Color(0xFF1DA266),
    yel   = Color(0xFFCC8F00),
    red   = Color(0xFFCC3333),
    txt   = Color(0xFF0D1435),
    txt2  = Color(0xFF3A4F6E),
    txt3  = Color(0xFF6B82AA),
    bdr   = Color(0xFF3A5FD9).copy(alpha = 0.15f),
    navBg = Color(0xFFE8EDFF),
)

val LocalAppColors = staticCompositionLocalOf { DarkAppColors }
