package com.clap2esp.app

class AdaptiveThreshold {

    private var peakAverage = 7000.0
    private var rmsAverage = 1000.0
    private var highFreqAverage = 0.20

    private val alpha = 0.02

    fun update(features: SignalFeatures) {

        peakAverage =
            peakAverage * (1.0 - alpha) +
            features.peak * alpha

        rmsAverage =
            rmsAverage * (1.0 - alpha) +
            features.rms * alpha

        highFreqAverage =
            highFreqAverage * (1.0 - alpha) +
            features.highFrequencyRatio * alpha
    }

    fun requiredPeak(): Double {
        return peakAverage * 1.8
    }

    fun requiredRms(): Double {
        return rmsAverage * 1.6
    }

    fun requiredHighFrequency(): Double {
        return highFreqAverage + 0.10
    }

    fun reset() {

        peakAverage = 7000.0
        rmsAverage = 1000.0
        highFreqAverage = 0.20
    }
}
