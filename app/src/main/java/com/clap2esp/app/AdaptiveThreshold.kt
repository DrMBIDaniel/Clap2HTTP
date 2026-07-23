package com.clap2esp.app

import kotlin.math.max

class AdaptiveThreshold(
    private val settings: SettingsManager? = null
) {

    private var threshold = 7000.0

    fun update(features: SignalFeatures) {

        val signalTarget = when {

            features.clapFrequencyScore > 0.80 ->
                features.peak * 1.10

            features.clapFrequencyScore > 0.60 ->
                features.peak * 1.05

            else ->
                features.peak * 0.98
        }

        val learnedTarget =

            if (
                settings != null &&
                settings.isTrained()
            ) {
                settings.averagePeak() * 0.75
            } else {
                0.0
            }

        val target =
            max(signalTarget, learnedTarget)

        threshold =
            threshold * 0.97 +
                    target * 0.03

        threshold =
            threshold.coerceIn(
                3000.0,
                30000.0
            )
    }

    fun currentThreshold(): Double {

        return threshold
    }
}
