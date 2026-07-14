package com.clap2esp.app


data class SignalFeatures(

    // Максимальная амплитуда импульса
    val peak: Int,


    // Средняя энергия сигнала
    val rms: Double,


    // Переходы через ноль
    val zeroCrossings: Int,


    // Время атаки (насколько быстро появился звук)
    val attack: Int,


    // Время затухания
    val decay: Int,


    // Длина активного импульса
    val impulseWidth: Int,


    // Простая оценка высоких частот
    val highFrequencyRatio: Double,


    // Новый параметр:
    // насколько спектр похож на хлопок
    val clapFrequencyScore: Double

)
