package com.clap2esp.app

class ClapDetector {

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

            Logger.log(
                "Candidate peak=${features.peak} spectral=${features.spectralPeak.toInt()} score=${"%.2f".format(features.clapFrequencyScore)}"
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

        if (!waitingSecondClap) {
            return ClapType.NONE
        }

        val now = System.currentTimeMillis()

        if (pendingSingle && now - firstClapTime > singleTimeout) {

            waitingSecondClap = false
            pendingSingle = false

            Logger.log("SINGLE CLAP")

            return ClapType.SINGLE_CLAP
        }

        return ClapType.NONE
    }

    private fun isClap(f: SignalFeatures): Boolean {

        Logger.log(
            "peak=${f.peak} " +
            "rms=${f.rms.toInt()} " +
            "hf=${"%.2f".format(f.highFrequencyRatio)} " +
            "low=${"%.2f".format(f.lowBandEnergy)} " +
            "mid=${"%.2f".format(f.midBandEnergy)} " +
            "high=${"%.2f".format(f.highBandEnergy)} " +
            "spPeak=${"%.2f".format(f.spectralPeak)} " +
            "centroid=${"%.2f".format(f.spectralCentroid)} " +
            "flat=${"%.2f".format(f.spectralFlatness)} " +
            "width=${f.impulseWidth}"
        )

        var score = 0

        if (f.peak > 7000)
            score++

        if (f.rms > 900)
            score++

        if (f.highFrequencyRatio > 0.18)
            score++

        if (f.highBandEnergy > f.lowBandEnergy)
            score++

        if (f.midBandEnergy > f.lowBandEnergy * 0.60)
            score++

        if (f.spectralPeak > 1000)
            score++

        if (f.spectralCentroid > 1500)
            score++

        if (f.spectralFlatness > 0.15)
            score++

        if (f.impulseWidth < 450)
            score++

        if (f.attack < 200)
            score++

        if (f.clapFrequencyScore > 0.55)
            score++

        Logger.log("Decision score=$score")

        return score >= 7
    }
}
