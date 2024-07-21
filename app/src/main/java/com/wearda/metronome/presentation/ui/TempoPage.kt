package com.wearda.metronome.presentation.ui

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import com.google.android.horologist.compose.layout.fillMaxRectangle
import com.wearda.metronome.presentation.composables.getLifecycleState
import com.wearda.metronome.presentation.composables.getScreenShape
import com.wearda.metronome.presentation.util.tempoToInterval
import kotlin.math.min
import kotlinx.coroutines.launch

@Composable
fun TempoPage(
  canSetTempo: Boolean,
  isTicking: Boolean,
  tempo: Long,
  onSetTempo: (Long) -> Unit,
  onSetIsTicking: (Boolean) -> Unit,
) {
  val vibrator = useVibrator()
  val lifecycleState = getLifecycleState()
  val coroutineScope = rememberCoroutineScope()
  DisposableEffect(isTicking, vibrator, tempo, lifecycleState == Lifecycle.State.RESUMED) {
    if (!isTicking) return@DisposableEffect onDispose {}

    val job =
      coroutineScope.launch {
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

  TempoTapButton(
    modifier =
      Modifier.fillMaxSize()
        .border(width = 5.dp, color = MaterialTheme.colors.primary, shape = getScreenShape())
        .padding(5.dp),
    onTempoSet = onSetTempo,
    disabled = !canSetTempo,
  ) {
    TopTitle("Tap / Set Tempo")
    Box(modifier = Modifier.fillMaxRectangle()) {
      TempoPicker(
        disabled = !canSetTempo,
        tempo = tempo,
        onSetTempo = onSetTempo,
        modifier = Modifier.fillMaxSize(),
      )
      Button(
        modifier = Modifier.size(ButtonDefaults.SmallButtonSize).align(Alignment.BottomEnd),
        onClick = { onSetIsTicking(!isTicking) },
      ) {
        Icon(
          if (isTicking) Icons.Rounded.Stop else Icons.Rounded.PlayArrow,
          modifier = Modifier.size(ButtonDefaults.SmallIconSize),
          contentDescription = "start or stop metronome",
        )
      }
    }
  }
}

fun createTempoVibrationWaveform(tempo: Long): VibrationEffect =
  VibrationEffect.createWaveform(
    run {
      val beatInterval = tempoToInterval(tempo)
      val beatVibrateDuration = min(100.0, beatInterval.toFloat() * 0.5).toLong()
      listOf(beatVibrateDuration, beatInterval - beatVibrateDuration).toLongArray()
    },
    intArrayOf(255, 0),
    0,
  )

@Composable
fun useVibrator(): Vibrator {
  return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
    LocalContext.current.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
  } else {
    val vibratorManager =
      LocalContext.current.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
    vibratorManager.defaultVibrator
  }
}
