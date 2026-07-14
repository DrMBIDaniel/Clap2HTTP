package com.clap2esp.app

import kotlin.math.abs
import kotlin.math.sqrt


class SignalAnalyzer {


    fun analyze(
        buffer: ShortArray
    ): SignalFeatures {


        var peak = 0

        var sumSquares = 0.0

        var zeroCrossings = 0


        var highFrequencyEnergy = 0.0

        var totalEnergy = 0.0


        var attackIndex = buffer.size

        var lastStrongIndex = 0



        val attackThreshold = 6000



        for (i in buffer.indices) {


            val sample =
                buffer[i].toInt()


            val amplitude =
                abs(sample)



            // Peak

            if (amplitude > peak) {

                peak = amplitude

            }



            // RMS

            sumSquares +=
                sample * sample.toDouble()



            // Zero crossing

            if (i > 0) {


                val previous =
                    buffer[i - 1]


                if (
                    (previous < 0 && buffer[i] >= 0)
                    ||
                    (previous >= 0 && buffer[i] < 0)
                ) {

                    zeroCrossings++

                }

            }



            /*
            Быстрые изменения сигнала.
            Хлопок имеет много таких изменений.
            */

            if (i > 0) {


                val difference =
                    abs(
                        buffer[i].toInt()
                        -
                        buffer[i - 1].toInt()
                    )


                highFrequencyEnergy += difference

            }



            totalEnergy += amplitude



            // начало импульса

            if (
                amplitude > attackThreshold
                &&
                attackIndex == buffer.size
            ) {

                attackIndex = i

            }


            if (amplitude > 3000) {

                lastStrongIndex = i

            }


        }



        val rms =
            sqrt(
                sumSquares /
                buffer.size
            )



        val highFrequencyRatio =
            if (totalEnergy > 0) {

                highFrequencyEnergy /
                        totalEnergy

            } else {

                0.0

            }




        /*
        Первый вариант оценки
        "похожести на хлопок"

        Хлопок:
        - высокая амплитуда
        - быстрый фронт
        - много ВЧ
        - короткий импульс
        */


        val impulseWidth =
            if (lastStrongIndex > attackIndex) {

                lastStrongIndex - attackIndex

            } else {

                buffer.size

            }




        val clapFrequencyScore =
            calculateClapScore(
                peak,
                highFrequencyRatio,
                zeroCrossings,
                impulseWidth
            )




        return SignalFeatures(


            peak = peak,


            rms = rms,


            zeroCrossings =
                zeroCrossings,


            attack =
                attackIndex,


            decay =
                buffer.size - lastStrongIndex,


            impulseWidth =
                impulseWidth,


            highFrequencyRatio =
                highFrequencyRatio,


            clapFrequencyScore =
                clapFrequencyScore

        )


    }







    private fun calculateClapScore(

        peak: Int,

        highFrequencyRatio: Double,

        zeroCrossings: Int,

        impulseWidth: Int

    ): Double {



        var score = 0.0



        // громкость

        if (peak > 10000) {

            score += 0.25

        }



        // резкие высокие частоты

        if (highFrequencyRatio > 0.35) {

            score += 0.35

        }



        // много быстрых переходов

        if (zeroCrossings > 30) {

            score += 0.20

        }



        // короткий импульс

        if (
            impulseWidth < 500
        ) {

            score += 0.20

        }



        return score

    }


}
