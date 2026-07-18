package com.clap2esp.app

import kotlin.math.abs
import kotlin.math.sqrt

class SignalAnalyzer {

    fun analyze(buffer: ShortArray): SignalFeatures {

        var peak = 0
        var sumSquares = 0.0
        var zeroCrossings = 0

        var diffEnergy = 0.0
        var totalEnergy = 0.0

        var attack = -1
        var lastStrong = -1

        val threshold = 5000

        for (i in buffer.indices) {

            val sample = buffer[i].toInt()
            val amplitude = abs(sample)

            if (amplitude > peak)
                peak = amplitude

            sumSquares += sample * sample.toDouble()

            totalEnergy += amplitude

            if (i > 0) {

                if ((buffer[i - 1] < 0 && buffer[i] >= 0) ||
                    (buffer[i - 1] >= 0 && buffer[i] < 0))
                    zeroCrossings++

                diffEnergy += abs(sample - buffer[i - 1].toInt())
            }

            if (amplitude > threshold) {

                if (attack == -1)
                    attack = i

                lastStrong = i
            }
        }

        if (attack == -1)
            attack = buffer.size

        if (lastStrong == -1)
            lastStrong = attack

        val rms =
            sqrt(sumSquares / buffer.size)

        val impulseWidth =
            (lastStrong - attack).coerceAtLeast(0)

        val decay =
            buffer.size - lastStrong

        val highFrequencyRatio =
            if (totalEnergy > 0.0)
                diffEnergy / totalEnergy
            else
                0.0

        val clapFrequencyScore =
            calculateScore(
                peak,
                rms,
                zeroCrossings,
                highFrequencyRatio,
                attack,
                impulseWidth
            )

        return SignalFeatures(
            peak = peak,
            rms = rms,
            zeroCrossings = zeroCrossings,
            attack = attack,
            decay = decay,
            impulseWidth = impulseWidth,
            highFrequencyRatio = highFrequencyRatio,
            clapFrequencyScore = clapFrequencyScore
        )
    }

    private fun calculateScore(
        peak: Int,
        rms: Double,
        zeroCrossings: Int,
        highFrequencyRatio: Double,
        attack: Int,
        impulseWidth: Int
    ): Double {

        var score = 0.0

        if (peak > 9000)
            score += 0.20

        if (rms > 1800)
            score += 0.15

        if (highFrequencyRatio > 0.45)
            score += 0.30

        if (zeroCrossings > 45)
            score += 0.15

        if (attack < 120)
            score += 0.10

        if (impulseWidth < 250)
            score += 0.10

        return score.coerceIn(0.0, 1.0)
    }
}
