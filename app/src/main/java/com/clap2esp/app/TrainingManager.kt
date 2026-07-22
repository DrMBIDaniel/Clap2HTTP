package com.clap2esp.app

class TrainingManager {

    companion object {
        private const val REQUIRED_SAMPLES = 40
    }

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

    fun averagePeak(): Double {

        return samples.map { it.peak }.average()
    }

    fun averageRms(): Double {

        return samples.map { it.rms }.average()
    }

    fun averageHighRatio(): Double {

        return samples.map { it.highRatio }.average()
    }

    fun averageFlux(): Double {

        return samples.map { it.spectralFlux }.average()
    }

    fun averageRollOff(): Double {

        return samples.map { it.spectralRollOff }.average()
    }

    fun averageCentroid(): Double {

        return samples.map { it.spectralCentroid }.average()
    }

    fun averageWidth(): Double {

        return samples.map { it.impulseWidth }.average()
    }

    fun averageZeroCrossings(): Double {

        return samples.map { it.zeroCrossings }.average()
    }
}
