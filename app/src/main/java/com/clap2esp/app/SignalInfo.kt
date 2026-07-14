package com.clap2esp.app


data class SignalInfo(

    // максимальная амплитуда сигнала
    val peak: Int,


    // средняя энергия сигнала
    val rms: Double,


    // количество переходов через ноль
    val zeroCrossings: Int,


    // скорость начала звука
    val attack: Int,


    // скорость затухания
    val decay: Int

)
