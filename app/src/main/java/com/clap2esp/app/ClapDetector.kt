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

        adaptiveThreshold.update(features)

        if (!isClap(features)) {
            return ClapType.NONE
        }

        val now = System.currentTimeMillis()

        if (!waitingSecondClap) {

            waitingSecondClap = true
            pendingSingle = true
            firstClapTime = now

            Logger.log(
                "CLAP CANDIDATE threshold=${adaptiveThreshold.currentThreshold().toInt()}"
            )

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

        if (!waitingSecondClap)
            return ClapType.NONE

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

        if (!noiseEstimator.isInitialized())
            return false

        val dynamicThreshold =
            adaptiveThreshold.currentThreshold()

        var score = 0

        if (f.peak > dynamicThreshold)
            score++

        if (f.rms > noiseEstimator.noiseRms() * 2.2)
            score++

        if (f.highFrequencyRatio >
            noiseEstimator.noiseHighRatio() + 0.08)
            score++

        if (f.highBandEnergy > f.lowBandEnergy)
            score++

        if (f.spectralPeak > 1800.0)
            score++

        if (f.spectralCentroid > 1200.0)
            score++

        if (f.spectralFlatness > 0.18)
            score++

        if (f.impulseWidth < 450)
            score++

        if (f.attack < 200)
            score++

        if (f.zeroCrossings > 20)
            score++

        if (f.clapFrequencyScore > 0.55)
            score++

        Logger.log(
            "Peak=${f.peak} Thr=${dynamicThreshold.toInt()} Score=$score"
        )

        return score >= 7
    }
}
