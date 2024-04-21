package com.wearda.metronome.presentation.theme

import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors

val PrimaryColor = Color(0xff9ccc65)
val PrimaryLightColor = Color(0xffcfff95)
val PrimaryDarkColor = Color(0xff6b9b37)
val SecondaryColor = Color(0xff80cbc4)
val SecondaryLightColor = Color(0xffb2fef7)
val SecondaryDarkColor = Color(0xff4f9a94)

internal val wearColorPalette: Colors = Colors(
  primary = PrimaryColor,
  primaryVariant = PrimaryLightColor,
  secondary = SecondaryColor,
  secondaryVariant = SecondaryDarkColor,
  error = Color.Red,
  onPrimary = Color.Black,
  onSecondary = Color.Black,
  onError = Color.Black
)

