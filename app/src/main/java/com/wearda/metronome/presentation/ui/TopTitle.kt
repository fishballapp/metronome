package com.wearda.metronome.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.wear.compose.foundation.CurvedLayout
import androidx.wear.compose.foundation.CurvedTextStyle
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.curvedText
import com.wearda.metronome.presentation.composables.getScreenShape

@Composable
fun TopTitle(title: String, modifier: Modifier = Modifier) {
  val textStyle = MaterialTheme.typography.caption2
  val mod = modifier.zIndex(50f)
  if (getScreenShape() === CircleShape) {
    val curvedTextStyle = CurvedTextStyle(textStyle)
    CurvedLayout(modifier = mod) { curvedText(title, style = curvedTextStyle) }
  } else {
    Row(
      modifier = mod.fillMaxWidth().background(MaterialTheme.colors.background),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.Top,
    ) {
      Text(title, style = textStyle)
    }
  }
}
