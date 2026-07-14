package com.clap2esp.app


data class SignalFeatures(

    // Максимальная громкость сигнала
    val amplitude: Int,


    // Сколько раз сигнал пересекает ноль
    // Используется для отличия шума и речи
    val zeroCrossingRate: Int,


    // Доля высоких частот
    // Пока будет заполняться анализатором
    val highFrequencyRatio: Float,


    // Время резкого подъёма звука
    // В миллисекундах
    val attackTime: Long,


    // Длина звукового импульса
    val duration: Long

)
