package com.wearda.metronome.presentation.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Switch
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.ToggleChip
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.layout.ScalingLazyColumn
import com.google.android.horologist.compose.layout.ScalingLazyColumnDefaults
import com.google.android.horologist.compose.layout.rememberResponsiveColumnState
import com.wearda.metronome.presentation.composables.getScreenShape
import com.wearda.metronome.presentation.compositionlocals.LocalUserSettings
import com.wearda.metronome.presentation.models.UserSettings
import kotlinx.coroutines.launch

@OptIn(ExperimentalHorologistApi::class)
@Composable
fun SettingsPage() {
  val coroutineScope = rememberCoroutineScope()
  val localUserSettings = LocalUserSettings.current
  val userPreferences by
    localUserSettings.userPreferencesFlow.collectAsState(initial = UserSettings(true, 1))

  Box(
    modifier =
      Modifier.fillMaxSize()
        .border(width = 5.dp, color = MaterialTheme.colors.surface, shape = getScreenShape())
        .padding(5.dp)
  ) {
    TopTitle(title = "Settings")
    ScalingLazyColumn(
      modifier = Modifier.fillMaxSize(),
      columnState =
        rememberResponsiveColumnState(
          contentPadding =
            ScalingLazyColumnDefaults.padding(
              first = ScalingLazyColumnDefaults.ItemType.Text,
              last = ScalingLazyColumnDefaults.ItemType.Chip,
            )
        ),
    ) {
      item {
        ToggleChip(
          modifier = Modifier.fillMaxWidth(),
          label = { Text("Freeze tempo while running") },
          checked = false,
          onCheckedChange = {
            coroutineScope.launch {
              localUserSettings.updateFreezeTempo(!userPreferences.freezeTempo)
            }
          },
          toggleControl = { Switch(checked = userPreferences.freezeTempo) },
        )
      }

      item {
        Chip(
          modifier = Modifier.fillMaxWidth(),
          colors = ChipDefaults.secondaryChipColors(),
          label = { Text("Tempo step size") },
          secondaryLabel = { Text(userPreferences.tempoStepSize.toString()) },
          onClick = {
            coroutineScope.launch {
              localUserSettings.updateTempoStepSize(
                when (userPreferences.tempoStepSize) {
                  1 -> 2
                  2 -> 5
                  5 -> 10
                  10 -> 1
                  else -> 1
                }
              )
            }
          },
        )
      }

      item {
        PlayStoreActionChip(
          colors =
            ChipDefaults.outlinedChipColors(contentColor = MaterialTheme.colors.onBackground),
          border = ChipDefaults.outlinedChipBorder(borderColor = MaterialTheme.colors.onBackground),
        )
      }
    }
  }
}
