package com.wearda.metronome.presentation.ui

import android.util.Log
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.google.android.horologist.compose.pager.PagerScreen
import com.wearda.metronome.presentation.DEFAULT_TEMPO
import com.wearda.metronome.presentation.compositionlocals.LocalUserSettings
import com.wearda.metronome.presentation.models.DEFAULT_USER_SETTINGS
import com.wearda.metronome.presentation.theme.MetronomeTheme

class AppContext(val setKeepScreenOn: (Boolean) -> Unit)

val LocalAppContext = compositionLocalOf {
  AppContext(
    setKeepScreenOn = { Log.e("LocalAppContext", "setKeepScreenOn is called but not provided") }
  )
}

@OptIn(ExperimentalWearFoundationApi::class)
@Composable
fun MetronomeApp(setKeepScreenOn: (Boolean) -> Unit = {}) {
  var tempo by remember { mutableLongStateOf(DEFAULT_TEMPO) }
  var isTicking by remember { mutableStateOf(false) }

  val navController = rememberSwipeDismissableNavController()

  CompositionLocalProvider(LocalAppContext provides AppContext(setKeepScreenOn)) {
    MetronomeTheme {
      SwipeDismissableNavHost(navController = navController, startDestination = "tempoSet") {
        composable("tempoSet") {
          val userSettings by
            LocalUserSettings.current.userPreferencesFlow.collectAsState(
              initial = DEFAULT_USER_SETTINGS
            )
          val pagerState = rememberPagerState { 2 }
          PagerScreen(state = pagerState) {
            when (it) {
              0 -> {
                TempoPage(
                  disablePicker = userSettings.pickerDisabled,
                  isTicking = isTicking,
                  tempo = tempo,
                  canSetTempo = !(userSettings.freezeTempo && isTicking),
                  onSetTempo = { tempo = it },
                  onSetIsTicking = {
                    if (!it) {
                      isTicking = false
                      return@TempoPage
                    }
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
                        isTicking = true
                      }
                    }
                  },
                )
              }
              1 -> {
                SettingsPage()
              }
            }
          }
        }

        run {
          val onProceed: () -> Unit = {
            navController.popBackStack()
            isTicking = true
          }

          val onDismiss: () -> Unit = { navController.popBackStack() }

          composable("slowTempoAlert") {
            SlowTempoAlert(tempo = tempo, onProceed = onProceed, onDismiss = onDismiss)
          }

          composable("fastTempoAlert") {
            FastTempoAlert(tempo = tempo, onProceed = onProceed, onDismiss = onDismiss)
          }

          composable("zeroBPMAlert") { ZeroBPMAlert(onDismiss) }
        }
      }
    }
  }
}
