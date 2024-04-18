package com.wearda.metronome.presentation.ui

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.OutlinedButton
import androidx.wear.tooling.preview.devices.WearDevices
import com.wearda.metronome.presentation.composables.getLifecycleState
import com.wearda.metronome.presentation.composables.getScreenShape
import com.wearda.metronome.presentation.theme.MetronomeTheme
import com.wearda.metronome.presentation.util.tempoToInterval
import kotlinx.coroutines.launch
import kotlin.math.min

@Composable
fun TickingPage(tempo: Long, onStop: () -> Unit) {
    val vibrator = useVibrator()
    val lifecycleState = getLifecycleState()
    val coroutineScope = rememberCoroutineScope()
    DisposableEffect(vibrator, tempo, lifecycleState == Lifecycle.State.RESUMED) {
        val job = coroutineScope.launch {
            if (lifecycleState == Lifecycle.State.RESUMED) {
                vibrator.vibrate(createTempoVibrationWaveform(tempo))
            }
        }

        onDispose {
            job.cancel()
            vibrator.cancel()
        }
    }

    val appContext = LocalAppContext.current
    DisposableEffect(appContext.setKeepScreenOn) {
        appContext.setKeepScreenOn(true)
        onDispose { appContext.setKeepScreenOn(false) }
    }

    TickingPageUi(tempo = tempo, onStop = onStop)
}

@Composable
fun TickingPageUi(tempo: Long, onStop: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .border(
                width = 5.dp,
                color = MaterialTheme.colors.primary,
                shape = getScreenShape()
            )
            .padding(5.dp),
    ) {
        TopTitle(title = "Tempo: $tempo")
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedButton(
                modifier = Modifier.size(ButtonDefaults.LargeButtonSize),
                onClick = onStop
            ) {
                Icon(
                    modifier = Modifier.size(ButtonDefaults.LargeIconSize),
                    imageVector = Icons.Rounded.Stop,
                    contentDescription = "stop metronome"
                )
            }
        }
    }
}

@Preview(device = WearDevices.LARGE_ROUND, showSystemUi = true)
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
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
        return LocalContext.current.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    } else {
        val vibratorManager =
            LocalContext.current.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        return vibratorManager.defaultVibrator
    }
}
