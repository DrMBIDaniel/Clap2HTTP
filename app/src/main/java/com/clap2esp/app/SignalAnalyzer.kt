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



        for (i in buffer.indices) {


            val sample =
                buffer[i].toInt()


            val amplitude =
                abs(sample)



            // Максимальная амплитуда
            if (amplitude > peak) {

                peak = amplitude

            }



            // RMS энергия

            sumSquares +=
                sample * sample.toDouble()



            // Zero crossing

            if (i > 0) {


                if (
                    (buffer[i - 1] < 0 &&
                     buffer[i] >= 0)
                    ||
                    (buffer[i - 1] >= 0 &&
                     buffer[i] < 0)
                ) {

                    zeroCrossings++

                }

            }



            /*
             Частотное приближение:

             быстрые изменения сигнала
             считаем высокими частотами
            */

            if (i > 1) {


                val difference =
                    abs(
                        buffer[i].toInt()
                        -
                        buffer[i - 1].toInt()
                    )


                highFrequencyEnergy += difference

            }



            totalEnergy += abs(sample)

        }



        val rms =
            sqrt(
                sumSquares /
                buffer.size
            )



        val highFrequencyRatio =
            if(totalEnergy > 0)
            {

                (
                    highFrequencyEnergy /
                    totalEnergy
                )

            }
            else
            {
                0.0
            }



        /*
        Пока используем
        простые оценки времени
        */


        val attack =
            estimateAttack(buffer)



        val decay =
            estimateDecay(buffer)



        val impulseWidth =
            buffer.size



        return SignalFeatures(

            peak = peak,

            rms = rms,

            zeroCrossings = zeroCrossings,

            attack = attack,

            decay = decay,

            impulseWidth = impulseWidth,

            highFrequencyRatio =
                highFrequencyRatio

        )

    }





    private fun estimateAttack(
        buffer: ShortArray
    ): Int {


        val threshold =
            5000


        for(i in buffer.indices)
        {

            if(
                abs(buffer[i].toInt())
                >
                threshold
            )
            {

                return i

            }

        }


        return buffer.size

    }





    private fun estimateDecay(
        buffer: ShortArray
    ): Int {


        val threshold =
            3000



        for(
            i in buffer.indices.reversed()
        )
        {


            if(
                abs(buffer[i].toInt())
                >
                threshold
            )
            {

                return buffer.size - i

            }

        }


        return buffer.size

    }

}
