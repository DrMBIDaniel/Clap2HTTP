package com.clap2esp.app

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.sin
import kotlin.math.sqrt

object FFT {

    fun magnitude(buffer: ShortArray): DoubleArray {

        val n = Integer.highestOneBit(buffer.size)

        val real = DoubleArray(n)
        val imag = DoubleArray(n)

        // Hann Window
        for (i in 0 until n) {

            val window =
                0.5 * (
                    1.0 -
                    cos(2.0 * PI * i / (n - 1))
                )

            real[i] =
                buffer[i].toDouble() * window
        }

        fft(real, imag)

        val spectrum = DoubleArray(n / 2)

        var max = 0.0

        for (i in spectrum.indices) {

            val value =
                sqrt(
                    real[i] * real[i] +
                    imag[i] * imag[i]
                )

            spectrum[i] = value

            if (value > max) {
                max = value
            }
        }

        // Нормализация
        if (max > 0.0) {

            for (i in spectrum.indices) {
                spectrum[i] /= max
            }
        }

        return spectrum
    }

    fun spectralPeak(spectrum: DoubleArray): Double {

        var peak = 0.0

        for (v in spectrum) {

            if (v > peak) {
                peak = v
            }
        }

        return peak
    }

    fun spectralCentroid(
        spectrum: DoubleArray,
        sampleRate: Double
    ): Double {

        var weighted = 0.0
        var total = 0.0

        for (i in spectrum.indices) {

            val freq =
                i * sampleRate / (spectrum.size * 2)

            weighted +=
                freq * spectrum[i]

            total +=
                spectrum[i]
        }

        if (total == 0.0)
            return 0.0

        return weighted / total
    }

    fun spectralFlatness(
        spectrum: DoubleArray
    ): Double {

        var geo = 0.0
        var arith = 0.0

        for (v in spectrum) {

            geo +=
                ln(v + 1e-12)

            arith +=
                v
        }

        geo =
            kotlin.math.exp(
                geo / spectrum.size
            )

        arith /=
            spectrum.size

        if (arith == 0.0)
            return 0.0

        return geo / arith
    }

    private fun fft(
        real: DoubleArray,
        imag: DoubleArray
    ) {

        val n = real.size

        var j = 0

        for (i in 0 until n) {

            if (i < j) {

                val tr = real[i]
                real[i] = real[j]
                real[j] = tr

                val ti = imag[i]
                imag[i] = imag[j]
                imag[j] = ti
            }

            var m = n shr 1

            while (j >= m && m >= 2) {
                j -= m
                m = m shr 1
            }

            j += m
        }

        var len = 2

        while (len <= n) {

            val angle =
                -2.0 * PI / len

            val wLenCos =
                cos(angle)

            val wLenSin =
                sin(angle)

            var start = 0

            while (start < n) {

                var wCos = 1.0
                var wSin = 0.0

                for (k in 0 until len / 2) {

                    val evenReal =
                        real[start + k]

                    val evenImag =
                        imag[start + k]

                    val oddReal =
                        real[start + k + len / 2] * wCos -
                        imag[start + k + len / 2] * wSin

                    val oddImag =
                        real[start + k + len / 2] * wSin +
                        imag[start + k + len / 2] * wCos

                    real[start + k] =
                        evenReal + oddReal

                    imag[start + k] =
                        evenImag + oddImag

                    real[start + k + len / 2] =
                        evenReal - oddReal

                    imag[start + k + len / 2] =
                        evenImag - oddImag

                    val nextCos =
                        wCos * wLenCos -
                        wSin * wLenSin

                    wSin =
                        wCos * wLenSin +
                        wSin * wLenCos

                    wCos =
                        nextCos
                }

                start += len
            }

            len = len shl 1
        }
    }
}
