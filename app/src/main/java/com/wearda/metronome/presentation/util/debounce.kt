package com.wearda.metronome.presentation.util

import androidx.compose.runtime.*
import com.wearda.metronome.presentation.composables.onPause
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun debounce(
    waitMs: Long = 300L,
    cb: () -> Unit,
): () -> Unit {
    val debounced = debounce<Unit>(waitMs) { cb() }
    return { debounced(Unit) }
}

@Composable
fun <T> debounce(
    waitMs: Long = 300L,
    cb: (T) -> Unit
): (T) -> Unit {
    val coroutineScope = rememberCoroutineScope()
    var debounceJob by remember { mutableStateOf<Job?>(null) }

    onPause { debounceJob?.cancel() }

    return {
        debounceJob?.cancel()
        debounceJob = coroutineScope.launch {
            delay(waitMs)
            cb(it)
        }
    }
}
