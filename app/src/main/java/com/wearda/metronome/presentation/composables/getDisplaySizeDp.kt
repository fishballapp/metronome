package com.wearda.metronome.presentation.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun getDisplayWidthDp(): Dp {
  val density = LocalDensity.current
  val context = LocalContext.current
  with(density) {
    return context.resources.displayMetrics.widthPixels.toDp()
  }
}

@Composable
fun getDpByDisplayWidthPercentage(widthPercentage: Int): Dp {
  return getDisplayWidthDp() * widthPercentage / 100
}

@Composable
fun getDisplayHeightDp(): Dp {
  val density = LocalDensity.current
  val context = LocalContext.current
  with(density) {
    return context.resources.displayMetrics.heightPixels.toDp()
  }
}

@Composable
fun getDpByDisplayHeightPercentage(heightPercentage: Int): Dp {
  return getDisplayWidthDp() * heightPercentage / 100
}
