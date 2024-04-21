package com.wearda.metronome.presentation.composables

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

class SimpleTargetBasedAnimationController(
  private val getValue: () -> Float,
  val start: () -> Unit,
) {
  val value: Float get() = getValue()
}

@Composable
fun simpleTargetBasedAnimation(
  initialValue: Float,
  targetValue: Float,
  animationSpec: AnimationSpec<Float>,
  onFinished: ((Float) -> Unit)? = null,
): SimpleTargetBasedAnimationController {
  val animatable = remember { Animatable(initialValue) }
  val coroutineScope = rememberCoroutineScope()

  return SimpleTargetBasedAnimationController(
    getValue = { animatable.value },
    start = {
      coroutineScope.launch {
        animatable.snapTo(initialValue)
        animatable.animateTo(targetValue, animationSpec)
        onFinished?.invoke(animatable.value)
      }
    },
  )
}
