package com.wearda.metronome.presentation.ui

import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.foundation.rememberActiveFocusRequester
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Picker
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.rememberPickerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalWearFoundationApi::class)
@Composable
fun TempoPicker(
  tempo: Long,
  onSetTempo: (Long) -> Unit,
  modifier: Modifier = Modifier,
) {
  val coroutineScope = rememberCoroutineScope()
  val tempoPickerState = rememberPickerState(
    initialNumberOfOptions = 300,
    initiallySelectedOption = remember { tempo.toInt() }
  )
  val focusRequester = rememberActiveFocusRequester()

  LaunchedEffect(tempo) {
    tempoPickerState.scrollToOption(tempo.toInt())
  }

  LaunchedEffect(tempoPickerState.isScrollInProgress, tempoPickerState.selectedOption) {
    if (!tempoPickerState.isScrollInProgress) {
      onSetTempo(tempoPickerState.selectedOption.toLong())
    }
  }

  Picker(
    modifier = modifier
      .onRotaryScrollEvent {
        coroutineScope.launch {
          tempoPickerState.scrollBy(
            it.verticalScrollPixels
          )
          tempoPickerState.animateScrollBy(
            0f
          )
        }
        true
      }
      .focusRequester(focusRequester)
      .focusable(),
    state = tempoPickerState,
    contentDescription = "Tempo",
  ) {
    Text(
      it.toString(),
      style = MaterialTheme.typography.display1,
    )
  }
}
