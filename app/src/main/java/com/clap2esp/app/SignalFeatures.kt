package com.clap2esp.app


data class SignalFeatures(

    // Максимальная амплитуда сигнала
    val peak: Int,


    // Средняя энергия сигнала
    val rms: Double,


    // Количество переходов через ноль
    val zeroCrossings: Int,


    // Скорость нарастания сигнала
    // меньше = больше похоже на хлопок
    val attack: Long,


    // Скорость затухания
    val decay: Long,


    // Длина импульса
    val impulseWidth: Long,


    // Частотный показатель
    // пока используется как запас под FFT
    val highFrequencyRatio: Double = 0.0

)
