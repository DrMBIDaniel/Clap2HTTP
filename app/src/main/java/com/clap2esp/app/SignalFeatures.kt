package com.clap2esp.app

data class SignalFeatures(

    val peak: Int,

    val rms: Double,

    val zeroCrossings: Int,

    val attack: Int,

    val decay: Int,

    val impulseWidth: Int,

    val highFrequencyRatio: Double,

    val clapFrequencyScore: Double,

    val lowBandEnergy: Double,

    val midBandEnergy: Double,

    val highBandEnergy: Double,

    val spectralPeak: Double,

    val spectralCentroid: Double,

    val spectralFlatness: Double,

    val spectralFlux: Double,

    val spectralRollOff: Double
)
