package com.clap2esp.app

object TrainingManager {

    private const val REQUIRED_SAMPLES = 40

    private val samples = mutableListOf<TrainingSample>()

    fun add(sample: TrainingSample) {

        if (samples.size >= REQUIRED_SAMPLES)
            return

        samples.add(sample)

        Logger.log(
            "Training sample ${samples.size}/$REQUIRED_SAMPLES"
        )
    }

    fun isFinished(): Boolean {

        return samples.size >= REQUIRED_SAMPLES
    }

    fun progress(): Int {

        return samples.size
    }

    fun clear() {

        samples.clear()
    }

    fun averagePeak() =
        samples.map { it.peak }.average()

    fun averageRms() =
        samples.map { it.rms }.average()

    fun averageHighRatio() =
        samples.map { it.highRatio }.average()

    fun averageFlux() =
        samples.map { it.spectralFlux }.average()

    fun averageRollOff() =
        samples.map { it.spectralRollOff }.average()

    fun averageCentroid() =
        samples.map { it.spectralCentroid }.average()

    fun averageWidth() =
        samples.map { it.impulseWidth }.average()

    fun averageZeroCrossings() =
        samples.map { it.zeroCrossings }.average()
}
