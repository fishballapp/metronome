package com.wearda.metronome.presentation.ui

import androidx.compose.animation.core.EaseInCirc
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.wearda.metronome.presentation.TEMPO_TAP_RESET_MS
import com.wearda.metronome.presentation.composables.getDisplayWidthDp
import com.wearda.metronome.presentation.composables.getScreenShape
import com.wearda.metronome.presentation.composables.simpleTargetBasedAnimation
import com.wearda.metronome.presentation.theme.MetronomeTheme

@Composable
fun TempoTapPage(
    tempoOrNull: Long?,
    onSetTempo: ((Long?) -> Unit),
    onStartTicking: (() -> Unit),
) {
    val borderAnimationController = simpleTargetBasedAnimation(
        initialValue = 5f,
        targetValue = getDisplayWidthDp().value / 2,
        animationSpec = tween(
            durationMillis = TEMPO_TAP_RESET_MS.toInt(),
            easing = EaseInCirc
        ),
        onFinished = { onStartTicking.invoke() }
    )

    TempoTapButton(
        modifier = Modifier
            .fillMaxSize()
            .border(
                width = borderAnimationController.value.dp,
                color = MaterialTheme.colors.primary,
                shape = getScreenShape()
            ),
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.background),
        onTempoSet = { newTempo ->
            borderAnimationController.start()
            onSetTempo.invoke(newTempo)
        }
    ) {
        Text(
            if (tempoOrNull != null) "Tempo: $tempoOrNull" else "Tap your tempo, yo",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true)
@Composable
fun TempoTapPagePreview() {
    MetronomeTheme {
        TempoTapPage(tempoOrNull = null, onSetTempo = {}, onStartTicking = {})
    }
}
