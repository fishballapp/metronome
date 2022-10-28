package com.wearda.metronome.presentation.ui

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.core.EaseInExpo
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.currentBackStackEntryAsState
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.wearda.metronome.presentation.TEMPO_TAP_RESET_MS
import com.wearda.metronome.presentation.composables.getDisplayWidthDp
import com.wearda.metronome.presentation.composables.getScreenShape
import com.wearda.metronome.presentation.composables.onPause
import com.wearda.metronome.presentation.composables.simpleTargetBasedAnimation
import com.wearda.metronome.presentation.theme.MetronomeTheme
import com.wearda.metronome.presentation.util.tempoToInterval
import kotlin.math.min

@Composable
fun MetronomeApp(setKeepScreenOn: (Boolean) -> Unit = {}) {
    var tempoOrNull by remember { mutableStateOf<Long?>(null) }

    val navController = rememberSwipeDismissableNavController()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    var isTicking =
        remember(currentBackStackEntry?.destination?.route) { currentBackStackEntry?.destination?.route == "ticking" }

    val vibrator = useVibrator()
    DisposableEffect(vibrator, isTicking, tempoOrNull) {
        val tempo = tempoOrNull
        if (tempo != null && isTicking) vibrator.vibrate(
            createTempoVibrationWaveform(tempo)
        )

        onDispose { vibrator.cancel() }
    }

    DisposableEffect(tempoOrNull) {
        if (tempoOrNull != null) setKeepScreenOn(true)

        onDispose { setKeepScreenOn(false) }
    }

    onPause {
        val lastTickingState = isTicking
        isTicking = false
        onResume {
            isTicking = lastTickingState
            if (!isTicking) tempoOrNull = null
        }
    }

    MetronomeTheme {
        SwipeDismissableNavHost(
            navController = navController, startDestination = "init"
        ) {
            composable("init") {
                val borderAnimationController = simpleTargetBasedAnimation(
                    initialValue = 0f,
                    targetValue = getDisplayWidthDp().value / 2,
                    animationSpec = tween(
                        durationMillis = TEMPO_TAP_RESET_MS.toInt(),
                        easing = EaseInExpo
                    ),
                    onFinished = { navController.navigate("ticking") }
                )
                TempoTapButton(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(
                            width = borderAnimationController.value.dp,
                            color = MaterialTheme.colors.background,
                            shape = getScreenShape()
                        ),
                    onTempoSet = { newTempo ->
                        borderAnimationController.start()
                        tempoOrNull = newTempo
                    }
                ) {
                    Text(
                        if (tempoOrNull != null) "Tempo: $tempoOrNull" else "Tap your tempo, yo",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            composable("ticking") {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("Tempo: $tempoOrNull")
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(onClick = {
                        navController.navigateUp()
                        tempoOrNull = null
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.Stop, contentDescription = "stop metronome"
                        )
                    }
                }
            }
        }
    }
}

fun createTempoVibrationWaveform(tempo: Long): VibrationEffect = VibrationEffect.createWaveform(
    run {
        val beatInterval = tempoToInterval(tempo)
        val beatVibrateDuration = min(100.0, beatInterval.toFloat() * 0.5).toLong()
        listOf(beatVibrateDuration, beatInterval - beatVibrateDuration).toLongArray()
    }, intArrayOf(255, 0), 0
)

@Composable
fun useVibrator(): Vibrator {
    return LocalContext.current.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
}