package com.wearda.metronome.presentation.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.tooling.preview.devices.WearDevices

@Composable
fun MetronomeTheme(
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colors = wearColorPalette, typography = Typography, content = content
  )
}

@Preview(device = WearDevices.LARGE_ROUND, showSystemUi = true)
@Composable
fun ColorPreview() {
  val boxModifier = Modifier.size(30.dp)
  MetronomeTheme {
    val colors = listOf(
      MaterialTheme.colors.primary,
      MaterialTheme.colors.primaryVariant,
      MaterialTheme.colors.secondary,
      MaterialTheme.colors.secondaryVariant,
      MaterialTheme.colors.background,
      MaterialTheme.colors.surface,
      MaterialTheme.colors.error,
      MaterialTheme.colors.onPrimary,
      MaterialTheme.colors.onSecondary,
      MaterialTheme.colors.onBackground,
      MaterialTheme.colors.onSurface,
      MaterialTheme.colors.onSurfaceVariant,
      MaterialTheme.colors.onError
    )
    Column(
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      for (chunkedColors in colors.chunked(4)) {
        Row {
          for (color in chunkedColors) {
            Box(modifier = boxModifier.background(color))
          }
        }
      }
    }
  }
}

