package com.wearda.metronome.presentation.util

import androidx.compose.runtime.*
import com.wearda.metronome.presentation.composables.onPause
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun debounce(
    waitMs: Long = 300L,
    destinationFunction: () -> Unit
): () -> Unit {
    val coroutineScope = rememberCoroutineScope()
    var debounceJob by remember { mutableStateOf<Job?>(null) }

    onPause { debounceJob?.cancel() }

    return {
        debounceJob?.cancel()
        debounceJob = coroutineScope.launch {
            delay(waitMs)
            destinationFunction()
        }
    }
}
