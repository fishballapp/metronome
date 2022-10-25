package com.wearda.metronome.presentation.ui

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
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
import androidx.wear.compose.material.Text
import com.wearda.metronome.presentation.TEMPO_TAP_RESET_MS
import com.wearda.metronome.presentation.composables.onPause
import com.wearda.metronome.presentation.theme.MetronomeTheme
import com.wearda.metronome.presentation.util.debounce
import com.wearda.metronome.presentation.util.tempoToInterval
import kotlin.math.min

@Composable
fun MetronomeApp(setKeepScreenOn: (Boolean) -> Unit = {}) {
    var tempoOrNull by remember { mutableStateOf<Long?>(null) }
    var isTicking by remember { mutableStateOf(false) }

    val vibrator = useVibrator()
    DisposableEffect(vibrator, isTicking, tempoOrNull) {
        val tempo = tempoOrNull
        if (tempo != null && isTicking)
            vibrator.vibrate(
                createTempoVibrationWaveform(tempo)
            )

        onDispose { vibrator.cancel() }
    }

    DisposableEffect(isTicking) {
        setKeepScreenOn(isTicking)
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

    val startVibrateDebounced = debounce(TEMPO_TAP_RESET_MS) { isTicking = true }

    MetronomeTheme {
        if (!isTicking) {
            TempoTapButton(modifier = Modifier.fillMaxSize(), onTempoSet = { newTempo ->
                tempoOrNull = newTempo
                startVibrateDebounced()
            }) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(if (tempoOrNull != null) "Tempo: $tempoOrNull" else "Tap your tempo, yo")
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("Tempo: $tempoOrNull")
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = {
                    isTicking = false
                    tempoOrNull = null
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Stop,
                        contentDescription = "stop metronome"
                    )
                }
            }
        }
    }
}


fun createTempoVibrationWaveform(tempo: Long): VibrationEffect =
    VibrationEffect.createWaveform(
        run {
            val beatInterval = tempoToInterval(tempo)
            val beatVibrateDuration = min(150.0, beatInterval.toFloat() * 0.5).toLong()
            listOf(beatVibrateDuration, beatInterval - beatVibrateDuration).toLongArray()
        },
        intArrayOf(255, 0),
        0
    )

@Composable
fun useVibrator(): Vibrator {
    return LocalContext.current.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
}