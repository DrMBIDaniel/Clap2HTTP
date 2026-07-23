package com.clap2esp.app

class ClapDetector(
    private val noiseEstimator: NoiseEstimator,
    private val adaptiveThreshold: AdaptiveThreshold,
    private val decisionSmoother: DecisionSmoother
) {

    private var waitingSecondClap = false
    private var pendingSingle = false
    private var firstClapTime = 0L

    private val minDoubleDelay = 90L
    private val maxDoubleDelay = 450L
    private val singleTimeout = 550L
    private var trainingFinishedLogged = false

    fun detect(features: SignalFeatures): ClapType {

        val score = calculateScore(features)

        adaptiveThreshold.update(features)

        val clapDetected =
            decisionSmoother.update(
                score >= 8 &&
                        features.peak >
                        adaptiveThreshold.currentThreshold()
            )

        if (!clapDetected) {
            return ClapType.NONE
        }

        if (!TrainingManager.isFinished()) {

    TrainingManager.add(
        TrainingSample(
            peak = features.peak,
            rms = features.rms,
            highRatio = features.highFrequencyRatio,
            zeroCrossings = features.zeroCrossings,
            impulseWidth = features.impulseWidth,
            spectralCentroid = features.spectralCentroid,
            spectralFlatness = features.spectralFlatness,
            spectralFlux = features.spectralFlux,
            spectralRollOff = features.spectralRollOff
        )
    )

    if (
        TrainingManager.isFinished() &&
        !trainingFinishedLogged
    ) {

        trainingFinishedLogged = true

        Logger.log("========== TRAINING COMPLETE ==========")
        Logger.log("Average Peak = ${TrainingManager.averagePeak()}")
        Logger.log("Average RMS = ${TrainingManager.averageRms()}")
        Logger.log("Average Flux = ${TrainingManager.averageFlux()}")
        Logger.log("Average RollOff = ${TrainingManager.averageRollOff()}")

        Logger.log("Recommended Min Peak = ${TrainingManager.recommendedMinPeak()}")
        Logger.log("Recommended Max Peak = ${TrainingManager.recommendedMaxPeak()}")
    }
}

        val now = System.currentTimeMillis()

        if (!waitingSecondClap) {

            waitingSecondClap = true
            pendingSingle = true
            firstClapTime = now

            Logger.log(
                "Candidate score=$score threshold=${adaptiveThreshold.currentThreshold()}"
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

    private fun calculateScore(f: SignalFeatures): Int {

        if (!noiseEstimator.isInitialized())
            return 0

        var score = 0

        if (f.rms > noiseEstimator.noiseRms() * 2.2)
            score++

        if (f.peak > noiseEstimator.noisePeak() * 2.0)
            score++

        if (
            f.highFrequencyRatio >
            noiseEstimator.noiseHighRatio() + 0.08
        )
            score++

        if (f.highBandEnergy > f.lowBandEnergy)
            score++

        if (f.zeroCrossings > 20)
            score++

        if (f.impulseWidth < 450)
            score++

        if (f.attack < 200)
            score++

        if (f.clapFrequencyScore > 0.55)
            score++

        if (f.spectralPeak > 0.60)
            score++

        if (f.spectralCentroid > 1500)
            score++

        if (f.spectralFlatness > 0.15)
            score++

        if (f.spectralFlux > 0.08)
            score++

        if (f.spectralRollOff > 2500)
            score++

        Logger.log(
            "Score=$score Threshold=${adaptiveThreshold.currentThreshold()}"
        )

        return score
    }
}
