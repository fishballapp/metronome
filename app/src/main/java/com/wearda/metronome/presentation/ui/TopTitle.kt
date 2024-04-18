package com.wearda.metronome.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.wear.compose.foundation.CurvedLayout
import androidx.wear.compose.foundation.CurvedTextStyle
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.curvedText
import com.wearda.metronome.presentation.composables.getScreenShape

@Composable
fun TopTitle(title: String, modifier: Modifier = Modifier) {
    val textStyle = MaterialTheme.typography.caption2
    if (getScreenShape() === CircleShape) {
        val curvedTextStyle = CurvedTextStyle(textStyle)
        CurvedLayout(modifier = modifier) {
            curvedText(title, style = curvedTextStyle)
        }
    } else {
        Row(
            modifier = modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                title,
                style = textStyle
            )
        }
    }
}