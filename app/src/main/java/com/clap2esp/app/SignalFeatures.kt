package com.clap2esp.app

data class SignalFeatures(

    // Максимальная амплитуда
    val peak: Int,

    // RMS энергия
    val rms: Double,

    // Количество переходов через ноль
    val zeroCrossings: Int,

    // Скорость нарастания импульса
    val attack: Int,

    // Скорость затухания
    val decay: Int,

    // Ширина импульса
    val impulseWidth: Int,

    // Доля высоких частот (0.0..1.0)
    val highFrequencyRatio: Double

)
