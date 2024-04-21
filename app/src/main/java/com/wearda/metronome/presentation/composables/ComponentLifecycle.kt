package com.wearda.metronome.presentation.composables

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner

class Destroyable(val onDestroy: (handler: () -> Unit) -> Unit)
class Stoppable(val onStop: (handler: () -> Unit) -> Unit)
class Resumable(val onResume: (handler: () -> Unit) -> Unit)
class Pauseable(val onPause: (handler: () -> Unit) -> Unit)

@Composable
fun getLifecycleState(): Lifecycle.State {
  val currentLifecycle = LocalLifecycleOwner.current.lifecycle
  var lifecycleState by remember { mutableStateOf(currentLifecycle.currentState) }
  val observer = remember {
    LifecycleEventObserver { owner, _ ->
      lifecycleState = owner.lifecycle.currentState
    }
  }
  DisposableEffect(currentLifecycle) {
    currentLifecycle.addObserver(observer)
    onDispose { currentLifecycle.removeObserver(observer) }
  }
  return lifecycleState
}

@Composable
fun LifecycleObserve(observerCb: (LifecycleOwner, Lifecycle.Event) -> Unit) {
  val currentLifecycle = LocalLifecycleOwner.current.lifecycle
  val observer = remember { LifecycleEventObserver(observerCb) }
  DisposableEffect(currentLifecycle, observer) {
    val lifecycle = currentLifecycle
    lifecycle.addObserver(observer)
    onDispose { lifecycle.removeObserver(observer) }
  }
}

@SuppressLint("ComposableNaming")
@Composable
fun onStart(handler: Stoppable.() -> Unit) {
  var onStop: (() -> Unit)? by remember { mutableStateOf(null) }
  LifecycleObserve { _, event ->
    when (event) {
      Lifecycle.Event.ON_START -> {
        with(Stoppable { onStop = it }) {
          handler()
        }
      }

      Lifecycle.Event.ON_STOP -> {
        onStop?.invoke()
      }

      else -> {}
    }
  }
}

@SuppressLint("ComposableNaming")
@Composable
fun onStop(handler: () -> Unit) {
  LifecycleObserve { _, event ->
    when (event) {
      Lifecycle.Event.ON_STOP -> {
        handler()
      }

      else -> {}
    }
  }
}

@SuppressLint("ComposableNaming")
@Composable
fun onCreate(handler: Destroyable.() -> Unit) {
  var onDestroy: (() -> Unit)? = null
  LifecycleObserve { _, event ->
    when (event) {
      Lifecycle.Event.ON_CREATE -> {
        with(Destroyable { onDestroy = it }) {
          handler()
        }
      }

      Lifecycle.Event.ON_DESTROY -> {
        onDestroy?.invoke()
      }

      else -> {}
    }
  }
}

@SuppressLint("ComposableNaming")
@Composable
fun onDestroy(handler: () -> Unit) {
  LifecycleObserve { _, event ->
    when (event) {
      Lifecycle.Event.ON_DESTROY -> {
        handler()
      }

      else -> {}
    }
  }
}

@SuppressLint("ComposableNaming")
@Composable
fun onPause(handler: Resumable.() -> Unit) {
  var onResume: (() -> Unit)? = null
  LifecycleObserve { _, event ->
    when (event) {
      Lifecycle.Event.ON_PAUSE -> {
        with(Resumable { onResume = it }) {
          handler()
        }
      }

      Lifecycle.Event.ON_RESUME -> {
        onResume?.invoke()
      }

      else -> {}
    }
  }
}

@SuppressLint("ComposableNaming")
@Composable
fun onResume(handler: Pauseable.() -> Unit) {
  var onPause: (() -> Unit)? = null
  LifecycleObserve { _, event ->
    when (event) {
      Lifecycle.Event.ON_RESUME -> {
        with(Pauseable(onPause = { onPause = it })) {
          handler()
        }
      }

      Lifecycle.Event.ON_PAUSE -> {
        onPause?.invoke()
      }

      else -> {}
    }
  }
}
