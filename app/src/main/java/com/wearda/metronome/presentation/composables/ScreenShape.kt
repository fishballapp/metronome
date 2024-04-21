package com.wearda.metronome.presentation.composables

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun getScreenShape(): Shape {
  return if (LocalConfiguration.current.isScreenRound) CircleShape else RectangleShape
}