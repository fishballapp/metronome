package com.ycmjason.metronome.presentation.composables

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

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
    val observer = object : DefaultLifecycleObserver {
        var onDestroy2 = {}
        var onStop2 = {}
        var onResume2 = {}
        override fun onPause(owner: LifecycleOwner) {
            super.onPause(owner)
            with(Resumable { onResume2 = it }) {
                onPause()
            }
        }

        override fun onResume(owner: LifecycleOwner) {
            super.onResume(owner)
            onResume()
            onResume2()
        }

        override fun onStart(owner: LifecycleOwner) {
            super.onStart(owner)
            with(Stoppable { onStop2 = it }) {
                onStart()
            }
        }

        override fun onStop(owner: LifecycleOwner) {
            super.onStop(owner)
            onStop()
            onStop2()
        }

        override fun onCreate(owner: LifecycleOwner) {
            super.onCreate(owner)
            with(Destroyable { onDestroy2 = it }) {
                onCreate()
            }
        }

        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            onDestroy()
            onDestroy2()
        }
    }
    DisposableEffect(lifecycleOwner, observer) {
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
