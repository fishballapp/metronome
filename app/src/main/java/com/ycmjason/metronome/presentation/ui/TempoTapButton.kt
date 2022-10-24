package com.ycmjason.metronome.presentation.ui

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import com.ycmjason.metronome.presentation.TEMPO_TAP_RESET_MS
import com.ycmjason.metronome.presentation.util.getEpochNow
import com.ycmjason.metronome.presentation.util.intervalToTempoBpm

@Composable
fun TempoTapButton(
    modifier: Modifier = Modifier,
    onTempoSet: (timeElapsed: Long) -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    var tapTimes by remember { mutableStateOf(listOf<Long>()) }

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
                        tapTimes.zip(tapTimes.drop(1)).map { (t1, t2) -> t2 - t1 }.average()
                            .toLong()
                    )
                )
            }
        },
        content = content
    )
}
