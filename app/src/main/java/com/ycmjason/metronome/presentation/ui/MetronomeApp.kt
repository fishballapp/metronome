package com.ycmjason.metronome.presentation.ui

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
import com.ycmjason.metronome.presentation.TEMPO_TAP_RESET_MS
import com.ycmjason.metronome.presentation.composables.onPause
import com.ycmjason.metronome.presentation.theme.MetronomeTheme
import com.ycmjason.metronome.presentation.util.debounce
import com.ycmjason.metronome.presentation.util.tempoToInterval
import kotlin.math.min

@Composable
fun MetronomeApp(setKeepScreenOn: (Boolean) -> Unit = {}) {
    var tempoOrNull by remember { mutableStateOf<Long?>(null) }
    var isTicking by remember { mutableStateOf(false) }

    val vibrator = useVibrator()
    DisposableEffect(vibrator, isTicking, tempoOrNull) {
        val tempo = tempoOrNull ?: return@DisposableEffect onDispose {}

        if (isTicking) vibrator.vibrate(
            createTempoVibrationWaveform(beatsPerBar = 4, tempo = tempo)
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
        }
    }

    val startVibrateDebounced =
        debounce(TEMPO_TAP_RESET_MS, rememberCoroutineScope()) { isTicking = true }

    MetronomeTheme {
        TempoTapButton(modifier = Modifier.fillMaxSize(), onTempoSet = { newTempo ->
            tempoOrNull = newTempo
            startVibrateDebounced()
        }) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = if (tempoOrNull != null) "Tempo: $tempoOrNull" else "Tap your tempo, yo"
                )
                if (isTicking) {
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
}


fun createTempoVibrationWaveform(tempo: Long, beatsPerBar: Long): VibrationEffect =
    VibrationEffect.createWaveform(
        (1..beatsPerBar).flatMap {
            val beatInterval = tempoToInterval(tempo)
            val beatVibrateDuration = min(150.0, beatInterval.toFloat() * 0.5).toLong()
            listOf(beatVibrateDuration, beatInterval - beatVibrateDuration)
        }
            .toLongArray(),
        (1..beatsPerBar).flatMap { listOf(255, 0) }.toIntArray(),
        0
    )

@Composable
fun useVibrator(): Vibrator {
    return LocalContext.current.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
}