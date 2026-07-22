package com.clap2esp.app

class AdaptiveThreshold {

    private var threshold = 7000.0

    fun update(features: SignalFeatures) {

        val target = when {

            features.clapFrequencyScore > 0.80 ->
                features.peak * 1.10

            features.clapFrequencyScore > 0.60 ->
                features.peak * 1.05

            else ->
                features.peak * 0.98
        }

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
