package com.wearda.metronome.presentation.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.wearda.metronome.presentation.models.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserSettingsRepository(private val context: Context) {

  companion object {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val FREEZE_TEMPO_KEY = booleanPreferencesKey("freeze_tempo")
    private val TEMPO_STEP_SIZE_KEY = intPreferencesKey("tempo_step_size")
  }

  val userPreferencesFlow: Flow<UserSettings> =
    context.dataStore.data.map { preferences ->
      UserSettings(
        freezeTempo = preferences[FREEZE_TEMPO_KEY] ?: false,
        tempoStepSize = preferences[TEMPO_STEP_SIZE_KEY] ?: 1,
      )
    }

  suspend fun updateFreezeTempo(freezeTempo: Boolean) {
    context.dataStore.edit { preferences -> preferences[FREEZE_TEMPO_KEY] = freezeTempo }
  }

  suspend fun updateTempoStepSize(tempoStepSize: Int) {
    context.dataStore.edit { preferences -> preferences[TEMPO_STEP_SIZE_KEY] = tempoStepSize }
  }
}
