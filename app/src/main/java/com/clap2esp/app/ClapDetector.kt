package com.clap2esp.app

import kotlin.math.abs

class ClapDetector {

    // Минимальная громкость
    private val threshold = 18000

    // Минимальная пауза между хлопками
    private val cooldown = 600L

    private var lastClapTime = 0L


    fun detect(buffer: ShortArray): Boolean {

        var maxAmplitude = 0

        for (sample in buffer) {

            val amplitude = abs(sample.toInt())

            if (amplitude > maxAmplitude) {
                maxAmplitude = amplitude
            }
        }


        val currentTime = System.currentTimeMillis()


        /*
        Проверяем:
        1. звук достаточно громкий
        2. прошло время после прошлого хлопка
         */

        if (
            maxAmplitude > threshold &&
            currentTime - lastClapTime > cooldown
        ) {

            lastClapTime = currentTime


            Logger.log(
                "CLAP DETECTED amplitude=$maxAmplitude"
            )


            return true
        }


        return false
    }
}
