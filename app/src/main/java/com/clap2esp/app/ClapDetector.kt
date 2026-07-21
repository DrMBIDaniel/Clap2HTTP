package com.clap2esp.app

class ClapDetector(
    private val noiseEstimator: NoiseEstimator
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

            Logger.log("CLAP CANDIDATE")

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

        val rmsLimit =
            noiseEstimator.noiseRms() * 2.2

        val peakLimit =
            noiseEstimator.noisePeak() * 2.0

        var score = 0

        if (f.rms > rmsLimit)
            score++

        if (f.peak > peakLimit)
            score++

        if (f.highFrequencyRatio >
            noiseEstimator.noiseHighRatio() + 0.08)
            score++

        if (f.zeroCrossings > 20)
            score++

        if (f.impulseWidth < 450)
            score++

        if (f.attack < 200)
            score++

        if (f.clapFrequencyScore > 0.55)
            score++

        if (f.highBandEnergy > f.lowBandEnergy)
            score++

        if (f.spectralFlatness > 0.18)
            score++

        Logger.log(
            "NoiseRMS=${noiseEstimator.noiseRms().toInt()} " +
            "RMS=${f.rms.toInt()} " +
            "Score=$score"
        )

        return score >= 6
    }
}
