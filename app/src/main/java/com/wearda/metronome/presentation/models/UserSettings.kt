package com.wearda.metronome.presentation.models
data class UserSettings(val freezeTempo: Boolean, val pickerDisabled: Boolean)

val DEFAULT_USER_SETTINGS = UserSettings(freezeTempo = false, pickerDisabled = false)
