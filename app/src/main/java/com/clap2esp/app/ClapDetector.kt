package com.clap2esp.app

import kotlin.math.abs


class ClapDetector {


    // Чувствительность микрофона
    // Было 18000, уменьшаем для теста рядом с телефоном
    private val threshold = 12000


    // Защита от слишком частых срабатываний
    private val cooldown = 250L


    // Время ожидания второго хлопка
    private val doubleClapDelay = 700L


    private var lastClapTime = 0L


    private var waitingForSecondClap = false


    private var firstClapTime = 0L



    fun detect(buffer: ShortArray): Boolean {


        var maxAmplitude = 0


        // Находим самый громкий момент в аудиобуфере
        for (sample in buffer) {

            val amplitude = abs(sample.toInt())


            if (amplitude > maxAmplitude) {
                maxAmplitude = amplitude
            }
        }



        val currentTime = System.currentTimeMillis()



        // Слишком тихий звук
        if (maxAmplitude < threshold) {
            return false
        }



        // Защита от повторного срабатывания
        if (currentTime - lastClapTime < cooldown) {
            return false
        }



        lastClapTime = currentTime



        // Первый хлопок
        if (!waitingForSecondClap) {


            waitingForSecondClap = true

            firstClapTime = currentTime


            Logger.log(
                "First clap detected amplitude=$maxAmplitude"
            )


            return true
        }



        // Второй хлопок
        if (
            currentTime - firstClapTime <= doubleClapDelay
        ) {


            Logger.log(
                "Double clap detected amplitude=$maxAmplitude"
            )


            waitingForSecondClap = false


            return true
        }



        // Слишком поздно, начинаем новый цикл

        firstClapTime = currentTime


        return true

    }

}
