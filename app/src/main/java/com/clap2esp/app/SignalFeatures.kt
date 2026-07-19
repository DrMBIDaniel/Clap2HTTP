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

    // Энергия до 500 Гц
    val lowBandEnergy: Double,

    // Энергия 500–2000 Гц
    val midBandEnergy: Double,

    // Энергия выше 2000 Гц
    val highBandEnergy: Double,

    // Частота максимального спектрального пика
    val spectralPeak: Double
)
