package com.wearda.metronome.presentation.util

fun intervalToTempoBpm(interval: Long) = 1000 * 60 / interval
fun tempoToInterval(tempoBpm: Long) = 1000 * 60 / tempoBpm
