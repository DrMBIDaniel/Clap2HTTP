package com.clap2esp.app

import kotlin.math.abs


class ClapDetector {


    // Минимальная громкость для возможного хлопка
    private val threshold = 18000


    // Минимальная задержка между любыми срабатываниями
    private val cooldown = 400L


    // Максимальное время между двумя хлопками
    private val doubleClapDelay = 700L


    private var lastClapTime = 0L


    private var firstClapWaiting = false


    private var doubleClapStartTime = 0L



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
        Если звук недостаточно громкий,
        это не хлопок
         */

        if (maxAmplitude < threshold) {
            return false
        }



        /*
        Защита от одного длинного громкого звука
         */

        if (currentTime - lastClapTime < cooldown) {
            return false
        }



        lastClapTime = currentTime



        /*
        Первый хлопок
         */

        if (!firstClapWaiting) {


            firstClapWaiting = true

            doubleClapStartTime = currentTime


            Logger.log(
                "First clap detected"
            )


            return true

        }



        /*
        Второй хлопок
         */

        if (
            currentTime - doubleClapStartTime <= doubleClapDelay
        ) {


            Logger.log(
                "Double clap detected"
            )


            firstClapWaiting = false


            return true

        }



        /*
        Если второй хлопок слишком поздний,
        начинаем заново
         */

        doubleClapStartTime = currentTime


        return true

    }

}
