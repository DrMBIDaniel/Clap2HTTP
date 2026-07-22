package com.clap2esp.app

import kotlin.math.abs

object TrainingManager {

    private const val REQUIRED_SAMPLES = 40

    private val samples = mutableListOf<TrainingSample>()

    fun add(sample: TrainingSample) {

        samples.add(sample)

        Logger.log(
            "Training sample ${samples.size}"
        )
    }

    fun progress(): Int {

        return filteredSamples().size
    }

    fun isFinished(): Boolean {

        return filteredSamples().size >= REQUIRED_SAMPLES
    }

    fun clear() {

        samples.clear()
    }

    private fun median(values: List<Double>): Double {

        if (values.isEmpty())
            return 0.0

        val sorted = values.sorted()

        return if (sorted.size % 2 == 0) {

            (
                sorted[sorted.size / 2] +
                sorted[sorted.size / 2 - 1]
            ) / 2.0

        } else {

            sorted[sorted.size / 2]
        }
    }

    private fun filteredSamples(): List<TrainingSample> {

        if (samples.size < 10)
            return samples

        val medianPeak =
            median(
                samples.map {
                    it.peak.toDouble()
                }
            )

        return samples.filter {

            abs(
                it.peak - medianPeak
            ) <= medianPeak * 0.5
        }
    }

    fun averagePeak(): Double =
        filteredSamples()
            .map { it.peak }
            .average()

    fun averageRms(): Double =
        filteredSamples()
            .map { it.rms }
            .average()

    fun averageHighRatio(): Double =
        filteredSamples()
            .map { it.highRatio }
            .average()

    fun averageFlux(): Double =
        filteredSamples()
            .map { it.spectralFlux }
            .average()

    fun averageRollOff(): Double =
        filteredSamples()
            .map { it.spectralRollOff }
            .average()

    fun averageCentroid(): Double =
        filteredSamples()
            .map { it.spectralCentroid }
            .average()

    fun averageWidth(): Double =
        filteredSamples()
            .map { it.impulseWidth }
            .average()

    fun averageZeroCrossings(): Double =
        filteredSamples()
            .map { it.zeroCrossings }
            .average()

    fun minPeak(): Int =
        filteredSamples()
            .minOf { it.peak }

    fun maxPeak(): Int =
        filteredSamples()
            .maxOf { it.peak }

    fun recommendedMinPeak(): Int =
        (averagePeak() * 0.65).toInt()

    fun recommendedMaxPeak(): Int =
        (averagePeak() * 1.45).toInt()
}
