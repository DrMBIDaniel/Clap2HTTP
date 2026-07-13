package com.clap2esp.app

import kotlin.math.abs


enum class ClapType {
    NONE,
    SINGLE_CLAP,
    DOUBLE_CLAP
}



class ClapDetector {


    // Чувствительность
    private val threshold = 11000



    // Минимальная задержка между хлопками
    private val minInterval = 120L



    // Максимальное окно двойного хлопка
    private val doubleWindow = 800L



    private var lastDetection = 0L



    private var firstClapTime = 0L


    private var waitingSecond = false



    // анализ формы сигнала

    private var previousAmplitude = 0


    private var peakStrength = 0



    fun detect(buffer: ShortArray): ClapType {


        var maxAmplitude = 0



        for(sample in buffer) {


            val value =
                abs(sample.toInt())


            if(value > maxAmplitude) {

                maxAmplitude = value

            }

        }




        val now =
            System.currentTimeMillis()




        /*
        Фильтр слишком тихих звуков
        */

        if(maxAmplitude < threshold) {

            previousAmplitude = maxAmplitude

            return ClapType.NONE

        }





        /*
        Анализ резкости импульса

        Хлопок обычно имеет быстрый скачок
        */


        val jump =
            maxAmplitude - previousAmplitude



        previousAmplitude =
            maxAmplitude




        if(jump < 3000) {


            return ClapType.NONE

        }




        /*
        Защита от дребезга
        */


        if(
            now - lastDetection < minInterval
        ) {

            return ClapType.NONE

        }



        lastDetection = now





        /*
        Первый хлопок
        */


        if(!waitingSecond) {


            waitingSecond = true


            firstClapTime = now



            Logger.log(
                "Possible clap amplitude=$maxAmplitude"
            )



            return ClapType.NONE

        }





        /*
        Второй хлопок
        */


        val delay =
            now - firstClapTime




        if(delay <= doubleWindow) {


            waitingSecond = false



            Logger.log(
                "DOUBLE CLAP v5 delay=${delay}ms"
            )



            return ClapType.DOUBLE_CLAP

        }





        firstClapTime = now



        return ClapType.NONE

    }





    fun checkSingleClapTimeout(): ClapType {


        if(
            waitingSecond &&
            System.currentTimeMillis()
            - firstClapTime > doubleWindow
        ) {


            waitingSecond = false



            Logger.log(
                "SINGLE CLAP v5"
            )



            return ClapType.SINGLE_CLAP

        }



        return ClapType.NONE

    }


}
