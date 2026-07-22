package com.clap2esp.app

class NoiseEstimator {

    private var initialized = false

    private var rms = 0.0
    private var peak = 0.0
    private var highRatio = 0.0

    private var frames = 0

    private val calibrationTimeMs = 3000L
    private val calibrationStart =
        System.currentTimeMillis()

    private val alpha = 0.03

    private var state =
        CalibrationState.CALIBRATING

    fun update(features: SignalFeatures) {

        if (state == CalibrationState.CALIBRATING) {

            rms += features.rms
            peak += features.peak
            highRatio += features.highFrequencyRatio

            frames++

            if (frames % 20 == 0) {

                Logger.log(
                    "Calibrating... ${
                        System.currentTimeMillis() - calibrationStart
                    } ms"
                )
            }

            if (
                System.currentTimeMillis() - calibrationStart >= calibrationTimeMs
            ) {

                rms /= frames
                peak /= frames
                highRatio /= frames

                initialized = true
                state = CalibrationState.READY

                Logger.log(
                    "Calibration complete\n" +
                    "Frames=$frames\n" +
                    "Noise RMS=${rms.toInt()}\n" +
                    "Noise Peak=${peak.toInt()}\n" +
                    "Noise HF=$highRatio"
                )
            }

            return
        }

        // Не обучаемся на хлопках

        if (
            features.peak > peak * 3.0 ||
            features.rms > rms * 3.0
        ) {
            return
        }

        // Медленная адаптация окружающего шума

        rms =
            rms * (1.0 - alpha) +
                    features.rms * alpha

        peak =
            peak * (1.0 - alpha) +
                    features.peak * alpha

        highRatio =
            highRatio * (1.0 - alpha) +
                    features.highFrequencyRatio * alpha
    }

    fun state(): CalibrationState {
        return state
    }

    fun isInitialized(): Boolean {
        return initialized
    }

    fun noiseRms(): Double {
        return rms
    }

    fun noisePeak(): Double {
        return peak
    }

    fun noiseHighRatio(): Double {
        return highRatio
    }
}
