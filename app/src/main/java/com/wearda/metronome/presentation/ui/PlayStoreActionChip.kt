package com.wearda.metronome.presentation.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Reviews
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipBorder
import androidx.wear.compose.material.ChipColors
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text

@Composable
fun PlayStoreActionChip(
  colors: ChipColors = ChipDefaults.primaryChipColors(),
  border: ChipBorder = ChipDefaults.chipBorder(),
) {
  val context = LocalContext.current
  Chip(
    modifier = Modifier.fillMaxWidth(),
    icon = {
      Icon(
        imageVector = Icons.Filled.Reviews,
        contentDescription = "Review",
        modifier = Modifier.size(ChipDefaults.IconSize).wrapContentSize(align = Alignment.Center),
      )
    },
    colors = colors,
    border = border,
    label = { Text("Review this app") },
    secondaryLabel = { Text("on Google Play Store", maxLines = 1) },
    onClick = {
      val webUri = Uri.parse("http://play.google.com/store/apps/details?id=${context.packageName}")
      val webIntent = Intent(Intent.ACTION_VIEW, webUri)
      context.startActivity(webIntent)
    },
  )
}
