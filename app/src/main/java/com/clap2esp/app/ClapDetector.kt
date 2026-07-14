package com.clap2esp.app


class ClapDetector {


    private var waitingSecondClap = false

    private var firstClapTime = 0L


    private var pendingSingle = false



    private val minDoubleDelay = 90L

    private val maxDoubleDelay = 450L

    private val singleTimeout = 550L





    fun detect(
        features: SignalFeatures
    ): ClapType {



        if (!isClap(features)) {

            return ClapType.NONE

        }



        val now =
            System.currentTimeMillis()



        if (!waitingSecondClap) {


            waitingSecondClap = true

            pendingSingle = true

            firstClapTime = now



            Logger.log(
                "Clap candidate score=${features.clapFrequencyScore}"
            )



            return ClapType.NONE

        }




        val delay =
            now - firstClapTime




        if (
            delay >= minDoubleDelay &&
            delay <= maxDoubleDelay
        ) {


            waitingSecondClap = false

            pendingSingle = false



            Logger.log(
                "DOUBLE CLAP delay=${delay}ms"
            )


            return ClapType.DOUBLE_CLAP

        }



        firstClapTime = now


        return ClapType.NONE

    }








    fun checkSingleClapTimeout(): ClapType {



        if (!waitingSecondClap) {

            return ClapType.NONE

        }



        val now =
            System.currentTimeMillis()



        if (
            pendingSingle &&
            now - firstClapTime > singleTimeout
        ) {


            waitingSecondClap = false

            pendingSingle = false



            Logger.log(
                "SINGLE CLAP detected"
            )


            return ClapType.SINGLE_CLAP

        }



        return ClapType.NONE

    }









    private fun isClap(
        f: SignalFeatures
    ): Boolean {



        if (f.peak < 9000) {

            return false

        }



        if (f.rms < 1500) {

            return false

        }



        if (
            f.clapFrequencyScore < 0.55
        ) {

            return false

        }



        if (
            f.attack > 250
        ) {

            return false

        }



        if (
            f.impulseWidth > 800
        ) {

            return false

        }



        return true

    }


}
