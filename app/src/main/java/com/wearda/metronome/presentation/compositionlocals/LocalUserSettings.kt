package com.wearda.metronome.presentation.compositionlocals

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import com.wearda.metronome.presentation.datastore.UserSettingsRepository

val LocalUserSettings: ProvidableCompositionLocal<UserSettingsRepository> = compositionLocalOf {
  throw Exception("No LocalUserSettings provided")
}
