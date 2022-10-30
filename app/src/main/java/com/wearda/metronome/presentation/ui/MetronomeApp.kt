package com.wearda.metronome.presentation.ui

import androidx.compose.runtime.*
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.wearda.metronome.presentation.theme.MetronomeTheme

class AppContext(val setKeepScreenOn: (Boolean) -> Unit)

val LocalAppContext = compositionLocalOf { AppContext(setKeepScreenOn = {}) }

@Composable
fun MetronomeApp(setKeepScreenOn: (Boolean) -> Unit = {}) {
    var tempoOrNull by remember { mutableStateOf<Long?>(null) }

    val navController = rememberSwipeDismissableNavController()

    CompositionLocalProvider(LocalAppContext provides AppContext(setKeepScreenOn)) {
        MetronomeTheme {
            SwipeDismissableNavHost(
                navController = navController,
                startDestination = "tempoTap"
            ) {
                composable("tempoTap") {
                    TempoTapPage(
                        tempoOrNull = tempoOrNull,
                        onSetTempo = { tempoOrNull = it },
                        onStartTicking = { navController.navigate("ticking") },
                    )
                }

                composable("ticking") {
                    val tempo = tempoOrNull
                    if (tempo == null) {
                        navController.navigateUp()
                        return@composable
                    }

                    TickingPage(tempo, onStop = { navController.navigate("tempoTap") })
                }
            }
        }
    }
}