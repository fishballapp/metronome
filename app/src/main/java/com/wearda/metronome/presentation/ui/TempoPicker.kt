package com.wearda.metronome.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Picker
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.rememberPickerState

@Composable
fun TempoPicker(
    tempo: Long,
    onSetTempo: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val tempoPickerState = rememberPickerState(
        initialNumberOfOptions = 300,
        initiallySelectedOption = remember { tempo.toInt() }
    )

    LaunchedEffect(tempo) {
        tempoPickerState.scrollToOption(tempo.toInt())
    }

    LaunchedEffect(tempoPickerState.isScrollInProgress, tempoPickerState.selectedOption) {
        if (!tempoPickerState.isScrollInProgress) {
            onSetTempo(tempoPickerState.selectedOption.toLong())
        }
    }

    Picker(
        modifier = modifier,
        state = tempoPickerState,
        contentDescription = "Tempo",
    ) {
        Text(
            it.toString(),
            style = MaterialTheme.typography.display1,
        )
    }
}
