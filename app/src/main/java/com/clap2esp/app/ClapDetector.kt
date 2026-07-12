package com.clap2esp.app

import kotlin.math.abs

class ClapDetector {


    // Порог громкости хлопка
    private val threshold = 15000


    // Защита от повторных срабатываний
    private var lastClapTime = 0L

    private val cooldown = 500L


    fun detect(buffer: ShortArray): Boolean {


        var maxAmplitude = 0


        for (sample in buffer) {

            val amplitude = abs(sample.toInt())

            if (amplitude > maxAmplitude) {
                maxAmplitude = amplitude
            }
        }


        val currentTime = System.currentTimeMillis()


        if (
            maxAmplitude > threshold &&
            currentTime - lastClapTime > cooldown
        ) {

            lastClapTime = currentTime

            return true
        }


        return false
    }
}
