package com.clap2esp.app


import kotlin.math.sqrt
import kotlin.math.PI
import kotlin.math.cos



class FFTAnalyzer {


    private val size = 1024



    fun getHighFrequencyEnergy(
        samples: ShortArray
    ): Double {


        val input = DoubleArray(size)



        for(i in 0 until size) {


            if(i < samples.size) {

                input[i] =
                    samples[i].toDouble()

            }

        }



        val spectrum =
            fft(input)



        var energy = 0.0



        /*
        Берём высокие частоты

        примерно 3000-8000 Hz
        */


        for(i in 200 until 500) {


            energy +=
                spectrum[i]

        }



        return energy

    }






    private fun fft(
        data: DoubleArray
    ): DoubleArray {


        val n =
            data.size


        val result =
            DoubleArray(n)



        for(k in 0 until n) {


            var real = 0.0

            var imag = 0.0



            for(t in 0 until n) {


                val angle =
                    2.0 *
                    PI *
                    t *
                    k /
                    n



                real +=
                    data[t] *
                    cos(angle)



                imag +=
                    data[t] *
                    kotlin.math.sin(angle)


            }



            result[k] =
                sqrt(
                    real * real +
                    imag * imag
                )

        }


        return result

    }


}
