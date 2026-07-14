package com.clap2esp.app


class ClapDetector {


    private var firstClapTime = 0L


    private val doubleClapWindow = 450L


    private var waitingForSecond = false



    fun detect(
        features: SignalFeatures
    ): ClapType {


        val now = System.currentTimeMillis()



        /*
        Фильтр голоса

        Голос обычно:
        - больше длительность
        - меньше атака
        - меньше высокочастотной энергии
        */


        if (
            features.highFrequencyRatio < 0.35 &&
            features.attackTime > 80
        ) {

            return ClapType.NONE

        }





        /*
        Главный фильтр хлопка

        Хлопок:
        - резкая атака
        - короткий импульс
        - высокая частота
        */


        val isClap =

            features.amplitude > 9000 &&
            features.attackTime < 60 &&
            features.duration < 250 &&
            features.highFrequencyRatio > 0.45





        if (!isClap) {

            return ClapType.NONE

        }





        /*
        Первый хлопок
        */


        if (!waitingForSecond) {


            waitingForSecond = true

            firstClapTime = now


            Logger.log(
                "Possible clap amplitude=${features.amplitude}"
            )


            return ClapType.NONE

        }







        /*
        Второй хлопок
        */


        val delay =
            now - firstClapTime



        waitingForSecond = false



        if (
            delay <= doubleClapWindow
        ) {


            Logger.log(
                "DOUBLE CLAP delay=${delay}ms"
            )


            return ClapType.DOUBLE_CLAP


        }





        return ClapType.NONE

    }






    fun checkSingleClapTimeout(): ClapType {


        if (
            waitingForSecond &&
            System.currentTimeMillis() -
            firstClapTime >
            doubleClapWindow
        ) {


            waitingForSecond = false


            Logger.log(
                "SINGLE CLAP detected"
            )


            return ClapType.SINGLE_CLAP

        }


        return ClapType.NONE

    }

}
