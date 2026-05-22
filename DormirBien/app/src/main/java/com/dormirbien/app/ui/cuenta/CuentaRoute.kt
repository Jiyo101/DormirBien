package com.dormirbien.app.ui.cuenta

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.dormirbien.app.ui.ajustes.AjustesRoute
import com.dormirbien.app.ui.perfil.PerfilRoute
import com.dormirbien.app.ui.theme.LocalAppColors

@Composable
fun CuentaRoute(onOpenSoundPicker: ((Uri) -> Unit) -> Unit) {
    val c = LocalAppColors.current
    var selectedTab by remember { mutableStateOf(0) }

    Column(Modifier.fillMaxSize().background(c.bg)) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor   = c.navBg,
            contentColor     = c.acc,
        ) {
            listOf("Perfil", "Ajustes").forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick  = { selectedTab = index },
                    text     = {
                        Text(
                            title,
                            color      = if (selectedTab == index) c.acc else c.txt3,
                            fontWeight = if (selectedTab == index) FontWeight.SemiBold else FontWeight.Normal,
                        )
                    },
                )
            }
        }
        when (selectedTab) {
            0 -> PerfilRoute()
            1 -> AjustesRoute(onOpenSoundPicker = onOpenSoundPicker)
        }
    }
}
