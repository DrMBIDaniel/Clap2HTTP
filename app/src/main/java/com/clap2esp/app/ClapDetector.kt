package com.clap2esp.app

import kotlin.math.abs


enum class ClapType {
    NONE,
    SINGLE_CLAP,
    DOUBLE_CLAP
}



class ClapDetector {


    // Чувствительность микрофона
    private val threshold = 12000


    // Минимальная задержка между импульсами
    private val minDoubleDelay = 120L


    // Максимальная задержка для двойного хлопка
    private val maxDoubleDelay = 800L


    // Защита от повторного одного и того же звука
    private val cooldown = 250L



    private var lastClapTime = 0L


    // Есть ли ожидающий первый хлопок
    private var waitingForSecond = false


    private var firstClapTime = 0L




    fun detect(buffer: ShortArray): ClapType {


        var maxAmplitude = 0



        for (sample in buffer) {

            val amplitude = abs(sample.toInt())


            if (amplitude > maxAmplitude) {
                maxAmplitude = amplitude
            }

        }



        val currentTime = System.currentTimeMillis()



        // Слишком тихо
        if (maxAmplitude < threshold) {
            return ClapType.NONE
        }



        // Защита от дребезга
        if (currentTime - lastClapTime < cooldown) {
            return ClapType.NONE
        }



        lastClapTime = currentTime




        /*
        Если это первый хлопок
        */

        if (!waitingForSecond) {


            waitingForSecond = true


            firstClapTime = currentTime



            Logger.log(
                "Waiting for second clap amplitude=$maxAmplitude"
            )


            return ClapType.NONE

        }




        /*
        Если пришёл второй хлопок
        */

        val delay =
            currentTime - firstClapTime



        if (
            delay >= minDoubleDelay &&
            delay <= maxDoubleDelay
        ) {


            waitingForSecond = false



            Logger.log(
                "DOUBLE CLAP delay=${delay}ms"
            )


            return ClapType.DOUBLE_CLAP

        }



        /*
        Второй хлопок слишком поздний.
        Старый становится одиночным.
        Новый становится первым.
        */


        firstClapTime = currentTime



        Logger.log(
            "SINGLE CLAP"
        )


        return ClapType.SINGLE_CLAP

    }



    /*
    Проверяем, не остался ли одиночный хлопок без пары
    */

    fun checkSingleTimeout(): ClapType {


        if (
            waitingForSecond &&
            System.currentTimeMillis() - firstClapTime > maxDoubleDelay
        ) {


            waitingForSecond = false


            Logger.log(
                "SINGLE CLAP timeout"
            )


            return ClapType.SINGLE_CLAP
        }



        return ClapType.NONE

    }

}
