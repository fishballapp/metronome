package com.ycmjason.metronome.presentation.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class Destroyable(val onDestroy: (handler: () -> Unit) -> Unit)
class Stopable(val onStop: (handler: () -> Unit) -> Unit)
class Resumable(val onResume: (handler: () -> Unit) -> Unit)

@Composable
fun ComponentLifecycle(
    onCreate: Destroyable.() -> Unit = {},
    onDestroy: () -> Unit = {},
    onStart: Stopable.() -> Unit = {},
    onStop: () -> Unit = {},
    onPause: Resumable.() -> Unit = {},
    onResume: () -> Unit = {},
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
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
                with(Stopable { onStop2 = it }) {
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
        val lifecycle = lifecycleOwner.lifecycle
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }
}

@Composable
fun OnStart(handler: Stopable.() -> Unit) {
    ComponentLifecycle(onStart = handler)
}

@Composable
fun OnStop(handler: () -> Unit) {
    ComponentLifecycle(onStop = handler)
}

@Composable
fun OnCreate(handler: Destroyable.() -> Unit) {
    ComponentLifecycle(onCreate = handler)
}

@Composable
fun OnDestroy(handler: () -> Unit) {
    ComponentLifecycle(onDestroy = handler)
}

@Composable
fun OnPause(handler: Resumable.() -> Unit) {
    ComponentLifecycle(onPause = handler)
}

@Composable
fun OnResume(handler: () -> Unit) {
    ComponentLifecycle(onResume = handler)
}
