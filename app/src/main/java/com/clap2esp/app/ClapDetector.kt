package com.clap2esp.app

class ClapDetector(
    private val noiseEstimator: NoiseEstimator,
    private val adaptiveThreshold: AdaptiveThreshold
) {

    private var waitingSecondClap = false
    private var pendingSingle = false
    private var firstClapTime = 0L

    private val minDoubleDelay = 90L
    private val maxDoubleDelay = 450L
    private val singleTimeout = 550L

    fun detect(features: SignalFeatures): ClapType {

        if (!isClap(features)) {
            return ClapType.NONE
        }

        val now = System.currentTimeMillis()

        if (!waitingSecondClap) {

            waitingSecondClap = true
            pendingSingle = true
            firstClapTime = now

            Logger.log("CLAP score=${score(features)}")

            return ClapType.NONE
        }

        val delay = now - firstClapTime

        if (delay in minDoubleDelay..maxDoubleDelay) {

            waitingSecondClap = false
            pendingSingle = false

            Logger.log("DOUBLE CLAP")

            return ClapType.DOUBLE_CLAP
        }

        firstClapTime = now

        return ClapType.NONE
    }

    fun checkSingleClapTimeout(): ClapType {

        if (!waitingSecondClap) {
            return ClapType.NONE
        }

        if (
            pendingSingle &&
            System.currentTimeMillis() - firstClapTime > singleTimeout
        ) {

            waitingSecondClap = false
            pendingSingle = false

            Logger.log("SINGLE CLAP")

            return ClapType.SINGLE_CLAP
        }

        return ClapType.NONE
    }

    private fun isClap(f: SignalFeatures): Boolean {

        if (!noiseEstimator.isInitialized()) {
            return false
        }

        val value = score(f)

        Logger.log("WeightedScore=$value")

        return value >= adaptiveThreshold.currentThreshold()
    }

    private fun score(f: SignalFeatures): Double {

        var score = 0.0

        if (f.peak > noiseEstimator.noisePeak() * 2.0)
            score += 2.5

        if (f.rms > noiseEstimator.noiseRms() * 2.2)
            score += 2.0

        if (f.highFrequencyRatio >
            noiseEstimator.noiseHighRatio() + 0.08)
            score += 2.0

        if (f.highBandEnergy > f.lowBandEnergy)
            score += 1.5

        if (f.spectralPeak > 1000.0)
            score += 1.5

        if (f.spectralCentroid > 1800.0)
            score += 1.5

        if (f.spectralFlatness > 0.18)
            score += 1.5

        if (f.impulseWidth < 450)
            score += 1.5

        if (f.attack < 200)
            score += 1.0

        if (f.clapFrequencyScore > 0.55)
            score += 2.0

        return score
    }
}
