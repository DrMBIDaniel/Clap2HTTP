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

    fun progress(): Int {

        return samples.size
    }

    fun isFinished(): Boolean {

        return samples.size >= REQUIRED_SAMPLES
    }

    fun clear() {

        samples.clear()
    }

    fun averagePeak(): Double =
        samples.map { it.peak }.average()

    fun averageRms(): Double =
        samples.map { it.rms }.average()

    fun averageHighRatio(): Double =
        samples.map { it.highRatio }.average()

    fun averageFlux(): Double =
        samples.map { it.spectralFlux }.average()

    fun averageRollOff(): Double =
        samples.map { it.spectralRollOff }.average()

    fun averageCentroid(): Double =
        samples.map { it.spectralCentroid }.average()

    fun averageWidth(): Double =
        samples.map { it.impulseWidth }.average()

    fun averageZeroCrossings(): Double =
        samples.map { it.zeroCrossings }.average()
}
