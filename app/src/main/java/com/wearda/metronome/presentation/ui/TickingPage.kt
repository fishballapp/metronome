package com.wearda.metronome.presentation.ui

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.wearda.metronome.presentation.theme.MetronomeTheme
import com.wearda.metronome.presentation.util.tempoToInterval
import kotlin.math.min

@Composable
fun TickingPage(tempo: Long, onStop: () -> Unit) {
    val vibrator = useVibrator()
    val appContext = LocalAppContext.current
    DisposableEffect(vibrator, tempo) {
        vibrator.vibrate(createTempoVibrationWaveform(tempo))
        onDispose { vibrator.cancel() }
    }

    DisposableEffect(tempo, appContext.setKeepScreenOn) {
        val setKeepScreenOn = appContext.setKeepScreenOn
        setKeepScreenOn(true)
        onDispose { setKeepScreenOn(false) }
    }

    TickingPageUi(tempo = tempo, onStop = onStop)
}

@Composable
fun TickingPageUi(tempo: Long, onStop: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Tempo: $tempo")
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = onStop) {
            Icon(
                imageVector = Icons.Rounded.Stop, contentDescription = "stop metronome"
            )
        }
    }
}

@Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true)
@Composable
fun TickingPagePreview() {
    MetronomeTheme {
        TickingPageUi(tempo = 67, onStop = {})
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
