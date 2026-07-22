package com.clap2esp.app

import kotlin.math.abs
import kotlin.math.sqrt

class SignalAnalyzer {
    private var previousSpectrum: DoubleArray? = null
    fun analyze(buffer: ShortArray): SignalFeatures {

        var peak = 0
        var sumSquares = 0.0
        var zeroCrossings = 0

        var attackIndex = buffer.size
        var lastStrongIndex = 0

        val attackThreshold = 6000

        for (i in buffer.indices) {

            val sample = buffer[i].toInt()
            val amplitude = abs(sample)

            if (amplitude > peak)
                peak = amplitude

            sumSquares += sample * sample.toDouble()

            if (i > 0) {

                val previous = buffer[i - 1]

                if (
                    (previous < 0 && buffer[i] >= 0) ||
                    (previous >= 0 && buffer[i] < 0)
                ) {
                    zeroCrossings++
                }
            }

            if (
                amplitude > attackThreshold &&
                attackIndex == buffer.size
            ) {
                attackIndex = i
            }

            if (amplitude > 3000)
                lastStrongIndex = i
        }

        val rms =
            sqrt(sumSquares / buffer.size)

        val spectrum =
            FFT.magnitude(buffer)

        val sampleRate = 44100.0
        val spectralFlux =
    FFT.spectralFlux(
        previousSpectrum,
        spectrum
    )

previousSpectrum =
    spectrum.copyOf()

val spectralRollOff =
    FFT.spectralRollOff(
        spectrum,
        sampleRate
    )

        var lowEnergy = 0.0
        var midEnergy = 0.0
        var highEnergy = 0.0

        var totalEnergy = 0.0

        for (i in spectrum.indices) {

            val value = spectrum[i]

            val freq =
                i * sampleRate / (spectrum.size * 2)

            totalEnergy += value

            when {

                freq < 500 ->
                    lowEnergy += value

                freq < 2000 ->
                    midEnergy += value

                else ->
                    highEnergy += value
            }
        }

        val highFrequencyRatio =
            if (totalEnergy > 0.0)
                highEnergy / totalEnergy
            else
                0.0

        val impulseWidth =
            if (lastStrongIndex > attackIndex)
                lastStrongIndex - attackIndex
            else
                buffer.size

        val clapFrequencyScore =
            calculateClapScore(
                peak,
                highFrequencyRatio,
                zeroCrossings,
                impulseWidth
            )

        return SignalFeatures(

            peak = peak,

    rms = rms,

    zeroCrossings = zeroCrossings,

    attack = attackIndex,

    decay = buffer.size - lastStrongIndex,

    impulseWidth = impulseWidth,

    highFrequencyRatio = highFrequencyRatio,

    clapFrequencyScore = clapFrequencyScore,

    lowBandEnergy = lowEnergy,

    midBandEnergy = midEnergy,

    highBandEnergy = highEnergy,

    spectralPeak =
        FFT.spectralPeak(spectrum),

    spectralCentroid =
        FFT.spectralCentroid(
            spectrum,
            sampleRate
        ),

    spectralFlatness =
        FFT.spectralFlatness(
            spectrum
        ),

    spectralFlux =
        spectralFlux,

    spectralRollOff =
        spectralRollOff
)
    }

    private fun calculateClapScore(
        peak: Int,
        highFrequencyRatio: Double,
        zeroCrossings: Int,
        impulseWidth: Int
    ): Double {

        var score = 0.0

        if (peak > 8000)
            score += 0.25

        if (highFrequencyRatio > 0.18)
            score += 0.35

        if (zeroCrossings > 20)
            score += 0.20

        if (impulseWidth < 450)
            score += 0.20

        return score.coerceIn(0.0, 1.0)
    }
}
