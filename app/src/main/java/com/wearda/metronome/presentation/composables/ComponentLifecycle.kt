package com.wearda.metronome.presentation.composables

import android.annotation.SuppressLint
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

class Destroyable(val onDestroy: (handler: () -> Unit) -> Unit)
class Stoppable(val onStop: (handler: () -> Unit) -> Unit)
class Resumable(val onResume: (handler: () -> Unit) -> Unit)

@Composable
fun ComponentLifecycle(
    onCreate: Destroyable.() -> Unit = {},
    onDestroy: () -> Unit = {},
    onStart: Stoppable.() -> Unit = {},
    onStop: () -> Unit = {},
    onPause: Resumable.() -> Unit = {},
    onResume: () -> Unit = {},
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var onDestroy2 by remember { mutableStateOf({}) }
    var onStop2 by remember { mutableStateOf({}) }
    var onResume2 by remember { mutableStateOf({}) }
    val observer = remember {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    with(Destroyable { onDestroy2 = it }) {
                        onCreate()
                    }
                }
                Lifecycle.Event.ON_DESTROY -> {
                    onDestroy()
                    onDestroy2()
                }
                Lifecycle.Event.ON_PAUSE -> {
                    with(Resumable { onResume2 = it }) {
                        onPause()
                    }
                }
                Lifecycle.Event.ON_RESUME -> {
                    onResume()
                    onResume2()
                }
                Lifecycle.Event.ON_START -> {
                    with(Stoppable { onStop2 = it }) {
                        onStart()
                    }
                }
                Lifecycle.Event.ON_STOP -> {
                    onStop()
                    onStop2()
                }
                else -> {}
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        val lifecycle = lifecycleOwner.lifecycle
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }
}

@SuppressLint("ComposableNaming")
@Composable
fun onStart(handler: Stoppable.() -> Unit) {
    ComponentLifecycle(onStart = handler)
}

@SuppressLint("ComposableNaming")
@Composable
fun onStop(handler: () -> Unit) {
    ComponentLifecycle(onStop = handler)
}

@SuppressLint("ComposableNaming")
@Composable
fun onCreate(handler: Destroyable.() -> Unit) {
    ComponentLifecycle(onCreate = handler)
}

@SuppressLint("ComposableNaming")
@Composable
fun onDestroy(handler: () -> Unit) {
    ComponentLifecycle(onDestroy = handler)
}

@SuppressLint("ComposableNaming")
@Composable
fun onPause(handler: Resumable.() -> Unit) {
    ComponentLifecycle(onPause = handler)
}

@SuppressLint("ComposableNaming")
@Composable
fun onResume(handler: () -> Unit) {
    ComponentLifecycle(onResume = handler)
}
