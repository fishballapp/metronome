package com.wearda.metronome.presentation.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.wearda.metronome.presentation.DEFAULT_TEMPO
import com.wearda.metronome.presentation.theme.MetronomeTheme

class AppContext(val setKeepScreenOn: (Boolean) -> Unit)

val LocalAppContext = compositionLocalOf {
  AppContext(setKeepScreenOn = {
    Log.e("LocalAppContext", "setKeepScreenOn is called but not provided")
  })
}


@Composable
fun MetronomeApp(setKeepScreenOn: (Boolean) -> Unit = {}) {
  var tempo by remember { mutableLongStateOf(DEFAULT_TEMPO) }

  val navController = rememberSwipeDismissableNavController()

  CompositionLocalProvider(LocalAppContext provides AppContext(setKeepScreenOn)) {
    MetronomeTheme {
      SwipeDismissableNavHost(
        navController = navController,
        startDestination = "tempoSet"
      ) {
        composable("tempoSet") {
          TempoSetPage(
            tempo = tempo,
            onSetTempo = { tempo = it },

            onStartTicking = {
              when {
                tempo <= 0L -> {
                  navController.navigate("zeroBPMAlert")
                }

                tempo < 20L -> {
                  navController.navigate("slowTempoAlert")
                }

                tempo > 200L -> {
                  navController.navigate("fastTempoAlert")
                }

                else -> {
                  navController.navigate("ticking")
                }
              }
            },
          )
        }

        run {
          val onProceed: () -> Unit = {
            navController.popBackStack()
            navController.navigate("ticking")
          }

          val onDismiss: () -> Unit = { navController.popBackStack() }
          composable("slowTempoAlert") {
            SlowTempoAlert(
              tempo = tempo,
              onProceed = onProceed,
              onDismiss = onDismiss
            )
          }

          composable("fastTempoAlert") {
            FastTempoAlert(
              tempo = tempo,
              onProceed = onProceed,
              onDismiss = onDismiss
            )
          }

          composable("zeroBPMAlert") {
            ZeroBPMAlert(onDismiss)
          }
        }

        composable("ticking") {
          TickingPage(tempo, onStop = {
            Log.v("TickingPage", "onStop")
            navController.navigateUp()
          })
        }
      }
    }
  }
}