package com.wearda.metronome.presentation.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pending
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyListItemScope
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.OutlinedChip
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.dialog.Alert

data class AppAlertDetail(
  val title: String,
  val paragraphs: List<String>,
  val icon: ImageVector,
  val actions: List<@Composable ScalingLazyListItemScope.() -> Unit>,
)

@Composable
fun ZeroBPMAlert(onDismiss: () -> Unit) {
  AppAlert(
    AppAlertDetail(
      title = "Time Stands Still",
      paragraphs =
        listOf(
          "Seems you've paused time itself with 0 BPM!",
          "Share your timeless experience while you wait for the first beat.",
        ),
      icon = Icons.Rounded.Pending,
      actions = listOf({ PlayStoreActionChip() }, { DismissActionChip("Later", onDismiss) }),
    )
  )
}

@Composable
fun SlowTempoAlert(onProceed: () -> Unit, onDismiss: () -> Unit, tempo: Long) {
  AppAlert(
    AppAlertDetail(
      title = "Slow Tempo",
      paragraphs =
        listOf("The tempo ($tempo BPM) is... unusually slow.", "Are you sure to proceed?"),
      icon = Icons.Rounded.Warning,
      actions = listOf({ ProceedActionChip(onProceed) }, { DismissActionChip("Back", onDismiss) }),
    )
  )
}

@Composable
fun FastTempoAlert(onProceed: () -> Unit, onDismiss: () -> Unit, tempo: Long) {
  AppAlert(
    AppAlertDetail(
      title = "Fast Tempo",
      paragraphs = listOf("The tempo ($tempo BPM) is... quite fast.", "Are you sure to proceed?"),
      icon = Icons.Rounded.Warning,
      actions = listOf({ ProceedActionChip(onProceed) }, { DismissActionChip("Back", onDismiss) }),
    )
  )
}

@Composable
fun ProceedActionChip(onProceed: () -> Unit) {
  Chip(modifier = Modifier.fillMaxWidth(), label = { Text("Proceed") }, onClick = onProceed)
}

@Composable
fun DismissActionChip(text: String, onDismiss: () -> Unit) {
  OutlinedChip(modifier = Modifier.fillMaxWidth(), label = { Text(text) }, onClick = onDismiss)
}

@Composable
private fun AppAlert(details: AppAlertDetail) {
  Alert(
    icon = {
      Icon(
        details.icon,
        modifier = Modifier.size(24.dp).wrapContentSize(align = Alignment.Center),
        contentDescription = "Alert Icon",
      )
    },
    title = { Text(text = details.title, textAlign = TextAlign.Center) },
    contentPadding = PaddingValues(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 80.dp),
  ) {
    items(details.paragraphs) { Text(it, textAlign = TextAlign.Center) }

    item { Spacer(modifier = Modifier.height(5.dp)) }

    items(details.actions) { it() }
  }
}
