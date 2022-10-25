package com.wearda.metronome.presentation.ui

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import com.wearda.metronome.presentation.TEMPO_TAP_RESET_MS
import com.wearda.metronome.presentation.composables.onPause
import com.wearda.metronome.presentation.util.getEpochNow
import com.wearda.metronome.presentation.util.intervalToTempoBpm

@Composable
fun TempoTapButton(
    modifier: Modifier = Modifier,
    onTempoSet: (timeElapsed: Long) -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    var tapTimes by remember { mutableStateOf(listOf<Long>()) }

    onPause { tapTimes = listOf() }

    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.background),
        onClick = {
            val now = getEpochNow()
            tapTimes = run {
                val timeElapsed = now - (tapTimes.lastOrNull() ?: 0)
                if (timeElapsed > TEMPO_TAP_RESET_MS) listOf(now) else tapTimes + now
            }

            if (tapTimes.size > 1) {
                onTempoSet(
                    intervalToTempoBpm(
                        tapTimes.zipWithNext { t1, t2 -> t2 - t1 }.average().toLong()
                    )
                )
            }
        },
        content = content
    )
}
