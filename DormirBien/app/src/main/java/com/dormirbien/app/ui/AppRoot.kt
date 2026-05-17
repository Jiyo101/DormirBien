package com.dormirbien.app.ui

import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.dormirbien.app.ui.components.ReviewDialog
import com.dormirbien.app.ui.cycles.CyclesRoute
import com.dormirbien.app.ui.history.HistoryRoute
import com.dormirbien.app.ui.home.HomeRoute
import com.dormirbien.app.ui.tips.TipsRoute

private val BG = Color(0xFF070B14)

private sealed class Dest(val route: String, val label: String, val icon: ImageVector) {
    object Home    : Dest("home",    "Inicio",    Icons.Default.Bedtime)
    object Cycles  : Dest("cycles",  "Ciclos",    Icons.Default.Loop)
    object History : Dest("history", "Historial", Icons.Default.CalendarMonth)
    object Tips    : Dest("tips",    "Consejos",  Icons.Default.Lightbulb)
}

private val DESTS = listOf(Dest.Home, Dest.Cycles, Dest.History, Dest.Tips)

// Same fade transition for every tab switch
private val ENTER  = fadeIn(tween(180))
private val EXIT   = fadeOut(tween(180))

@Composable
fun AppRoot(
    showReview:        Boolean,
    onReviewDismiss:   () -> Unit,
    onReviewSave:      (Int, String) -> Unit,
    onScheduleAlarms:  (Int, Int, Int, String) -> Unit,
    onCancelAlarms:    () -> Unit,
    onCancelBackup:    () -> Unit,
    onOpenSoundPicker: ((Uri) -> Unit) -> Unit,
) {
    if (showReview) {
        ReviewDialog(onDismiss = onReviewDismiss, onSave = onReviewSave)
    }

    val nav = rememberNavController()

    Scaffold(
        containerColor = BG,
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF0C1220), tonalElevation = 0.dp) {
                val back by nav.currentBackStackEntryAsState()
                val cur  = back?.destination
                DESTS.forEach { dest ->
                    NavigationBarItem(
                        selected = cur?.hierarchy?.any { it.route == dest.route } == true,
                        onClick  = {
                            nav.navigate(dest.route) {
                                popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState    = true
                            }
                        },
                        icon  = { Icon(dest.icon, null) },
                        label = { Text(dest.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor   = Color(0xFF5B7FFF),
                            selectedTextColor   = Color(0xFF5B7FFF),
                            unselectedIconColor = Color(0xFF3A4F6E),
                            unselectedTextColor = Color(0xFF3A4F6E),
                            indicatorColor      = Color(0xFF5B7FFF).copy(alpha = 0.12f),
                        )
                    )
                }
            }
        }
    ) { pad ->
        NavHost(
            navController       = nav,
            startDestination    = Dest.Home.route,
            enterTransition     = { ENTER },
            exitTransition      = { EXIT },
            popEnterTransition  = { ENTER },
            popExitTransition   = { EXIT },
            modifier            = Modifier.fillMaxSize().background(BG).padding(pad),
        ) {
            composable(Dest.Home.route) {
                val ctx = androidx.compose.ui.platform.LocalContext.current
                HomeRoute(
                    onScheduleAlarms  = onScheduleAlarms,
                    onCancelAlarms    = onCancelAlarms,
                    onCancelBackup    = onCancelBackup,
                    onOpenSoundPicker = onOpenSoundPicker,
                    onShowMiuiGuide   = {
                        try {
                            ctx.startActivity(android.content.Intent(
                                android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                android.net.Uri.parse("package:${ctx.packageName}")
                            ).addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK))
                        } catch (e: Exception) {}
                    },
                )
            }
            composable(Dest.Cycles.route)  { CyclesRoute() }
            composable(Dest.History.route) { HistoryRoute() }
            composable(Dest.Tips.route)    { TipsRoute() }
        }
    }
}
