package com.wearda.metronome.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.wearda.metronome.presentation.TEMPO_TAP_RESET_MS
import com.wearda.metronome.presentation.composables.onPause
import com.wearda.metronome.presentation.util.getEpochNow
import com.wearda.metronome.presentation.util.intervalToTempoBpm

@Composable
fun TempoTapButton(
  disabled: Boolean,
  modifier: Modifier = Modifier,
  onTempoSet: (timeElapsed: Long) -> Unit,
  content: @Composable BoxScope.() -> Unit = {},
) {
  var tapTimes by remember { mutableStateOf(listOf<Long>()) }

  onPause { tapTimes = listOf() }

  Box(
    modifier =
      modifier.clickable(onClickLabel = "tap for tempo", enabled = !disabled) {
        tapTimes = run {
          val now = getEpochNow()
          val timeElapsed = now - (tapTimes.lastOrNull() ?: 0)
          if (timeElapsed > TEMPO_TAP_RESET_MS) listOf(now) else tapTimes + now
        }

        if (tapTimes.size > 1) {
          onTempoSet(
            intervalToTempoBpm(tapTimes.zipWithNext { t1, t2 -> t2 - t1 }.average().toLong())
          )
        }
      },
    content = content,
  )
}
