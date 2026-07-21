package com.clap2esp.app

class NoiseEstimator {

    private var averageRms = 0.0
    private var averagePeak = 0.0
    private var averageHighRatio = 0.0

    private var initialized = false

    fun update(features: SignalFeatures) {

        if (!initialized) {

            averageRms = features.rms
            averagePeak = features.peak.toDouble()
            averageHighRatio = features.highFrequencyRatio

            initialized = true
            return
        }

        val alpha = 0.02

        averageRms =
            averageRms * (1.0 - alpha) +
            features.rms * alpha

        averagePeak =
            averagePeak * (1.0 - alpha) +
            features.peak * alpha

        averageHighRatio =
            averageHighRatio * (1.0 - alpha) +
            features.highFrequencyRatio * alpha
    }

    fun noiseRms(): Double {
        return averageRms
    }

    fun noisePeak(): Double {
        return averagePeak
    }

    fun noiseHighRatio(): Double {
        return averageHighRatio
    }

    fun isInitialized(): Boolean {
        return initialized
    }
}
