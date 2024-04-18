package com.wearda.metronome.presentation.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.tooling.preview.devices.WearDevices
import com.wearda.metronome.presentation.composables.getScreenShape
import com.wearda.metronome.presentation.theme.MetronomeTheme


@Composable
fun TempoSetPage(
    tempo: Long,
    onSetTempo: (Long) -> Unit,
    onStartTicking: ((Long) -> Unit),
) {
    TempoTapButton(
        modifier = Modifier
            .fillMaxSize()
            .border(
                width = 5.dp,
                color = MaterialTheme.colors.primary,
                shape = getScreenShape()
            )
            .padding(5.dp),
        onTempoSet = onSetTempo
    ) {
        TopTitle("Tap / Set Tempo")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 25.dp, bottom = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TempoPicker(tempo = tempo, onSetTempo = onSetTempo, modifier = Modifier.weight(1f))
            Button(
                onClick = { onStartTicking(tempo) }
            ) {
                Icon(
                    Icons.Rounded.PlayArrow,
                    contentDescription = "start metronome",
                )
            }
        }
    }
}

@Preview(device = WearDevices.LARGE_ROUND, showSystemUi = true)
@Composable
fun TempoSetPagePreview() {
    MetronomeTheme {
        TempoSetPage(tempo = 90, onSetTempo = {}, onStartTicking = {})
    }
}
