package com.wearda.metronome.presentation.models
data class UserSettings(val freezeTempo: Boolean, val tempoStepSize: Int)

val DEFAULT_USER_SETTINGS = UserSettings(true, 1)
